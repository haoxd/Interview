package com.bread.coalquality.mvc.controller;

import com.bread.coalquality.sys.config.influxdb.InfluxDBConnect;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @Description:mysqlvsinfluxdb
 * @Author: haoxd
 * @Version: 1.0
 */
@Slf4j
@RequestMapping("/mysqlvsinfluxdb")
@RestController
public class MySqlVsInfluxDBController {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    //测试数据量
    public static final int ROWS = 10000000;

    @Autowired
    private StandardEnvironment standardEnvironment;

    @Autowired
    private InfluxDBConnect influxDBConnect;


    @PostConstruct
    public void init() {
        //使用-Dspring.profiles.active=init启动程序进行初始化
        if (Arrays.stream(standardEnvironment.getActiveProfiles()).anyMatch(s -> s.equalsIgnoreCase("init"))) {
            initInfluxDB();
            initMySQL();
        }


    }

    //初始化MySQL
    private void initMySQL() {
        long begin = System.currentTimeMillis();
        jdbcTemplate.execute("DROP TABLE IF EXISTS `m`;");
        jdbcTemplate.execute("CREATE TABLE `m` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `value` bigint NOT NULL,\n" +
                "  `time` timestamp NOT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  KEY `time` (`time`) USING BTREE\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");

        String sql = "INSERT INTO `m` (`value`,`time`) VALUES (?,?)";
        //批量插入数据
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setLong(1, ThreadLocalRandom.current().nextInt(10000));
                preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now().minusSeconds(5 * i)));
            }

            @Override
            public int getBatchSize() {
                return ROWS;
            }
        });
        log.info("init mysql finished with count {} took {}ms", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM `m`", Long.class), System.currentTimeMillis() - begin);
    }

    //初始化InfluxDB
    private void initInfluxDB() {
        long begin = System.currentTimeMillis();
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient().newBuilder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        try (InfluxDB influxDB = InfluxDBFactory.connect("http://192.168.240.128:8086", "root", "root", okHttpClientBuilder)) {
            String db = "performance";
            influxDB.query(new Query("DROP DATABASE " + db));
            influxDB.query(new Query("CREATE DATABASE " + db));
            //设置数据库
            influxDB.setDatabase(db);
            //批量插入，10000条数据刷一次，或1秒刷一次
            influxDB.enableBatch(BatchOptions.DEFAULTS.actions(10000).flushDuration(1000));
            IntStream.rangeClosed(1, ROWS).mapToObj(i -> Point
                    .measurement("m")
                    .addField("value", ThreadLocalRandom.current().nextInt(10000))
                    .time(LocalDateTime.now().minusSeconds(5 * i).toInstant(ZoneOffset.UTC).toEpochMilli(), TimeUnit.MILLISECONDS).build())
                    .forEach(influxDB::write);
            influxDB.flush();
            log.info("init influxdb finished with count {} took {}ms", influxDB.query(new Query("SELECT COUNT(*) FROM m")).getResults().get(0).getSeries().get(0).getValues().get(0).get(1), System.currentTimeMillis() - begin);
        }
    }
    /**
     * 对这 1000 万数据进行一个统计，查询最近 60 天的数据，按照 1 小时的时间粒度聚合，统计 value 列的最大值、最小值和平均值，并将统计结果绘制成曲线图：
     *mysql :4075 ms
     *influxdb :260ms
     *
     * *
     *  总结一下，对于 MySQL 而言，针对大量的数据使用全表扫描的方式来聚合统计指标数据，性能非常差，一般只能作为临时方案来使用。
     * 此时，引入 InfluxDB 之类的时间序列数据库，就很有必要了。
     * 时间序列数据库可以作为特定场景（比如监控、统计）的主存储，也可以和关系型数据库搭配使用，作为一个辅助数据源，保存业务系统的指标数据。
     *
     *
     * */

    @GetMapping("mysql")
    public  List<Map<String, Object>> mysql() {
        long begin = System.currentTimeMillis();
        List<Map<String, Object>> result = jdbcTemplate.queryForList("SELECT date_format(time,'%Y%m%d%H'),max(value),min(value),avg(value) FROM m WHERE time>now()- INTERVAL 60 DAY GROUP BY date_format(time,'%Y%m%d%H')");
        log.info("took {} ms result {}", System.currentTimeMillis() - begin, result);
        return result;
    }

    @GetMapping("influxdb")
    public QueryResult influxdb() {
        long begin = System.currentTimeMillis();
        try (InfluxDB influxDB = InfluxDBFactory.connect("http://192.168.240.128:8086", "root", "root")) {
            influxDB.setDatabase("performance");
            QueryResult result = influxDB.query(new Query("SELECT MEAN(value),MIN(value),MAX(value) FROM m WHERE time > now() - 60d GROUP BY TIME(1h)"));

            log.info("took {} ms result {}", System.currentTimeMillis() - begin, result);
            return result;
        }
    }

    @GetMapping("influxdb2")
    public QueryResult influxdb2() {
        long begin = System.currentTimeMillis();


            QueryResult result = this.influxDBConnect.query("SELECT MEAN(value),MIN(value),MAX(value) FROM m WHERE time > now() - 60d GROUP BY TIME(1h)");

            log.info("took {} ms result {}", System.currentTimeMillis() - begin, result);
            return result;

    }

    @GetMapping("influxdbwrong")
    public void influxdbwrong() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient().newBuilder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);
        try (InfluxDB influxDB = InfluxDBFactory.connect("http://127.0.0.1:8086", "root", "root", okHttpClientBuilder)) {
            influxDB.setDatabase("performance");
            IntStream.rangeClosed(1, 10000).forEach(i -> {
                Map<String, String> tags = new HashMap<>();
                IntStream.rangeClosed(1, 10).forEach(j -> tags.put("tagkey" + i, "tagvalue" + ThreadLocalRandom.current().nextInt(100000)));
                Point point = Point.measurement("bad")
                        .tag(tags)
                        .addField("value", ThreadLocalRandom.current().nextInt(10000))
                        .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                        .build();
                influxDB.write(point);
            });
        }
    }
}
