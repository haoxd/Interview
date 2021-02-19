package com.bread.coalquality.mvc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Description:mysql对比redis
 * @Author: haoxd
 * @Version: 1.0
 */
@Slf4j
@RequestMapping("/mysqlvsredis")
@RestController
public class MySqlVSRedisController {

    public static final int ROWS = 100000;
    public static final String PAYLOAD = IntStream.rangeClosed(1, 100).mapToObj(__ -> "a").collect(Collectors.joining(""));




    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private StandardEnvironment standardEnvironment;



    @PostConstruct
    public void init() {
        //使用-Dspring.profiles.active=init启动程序进行初始化
        if (Arrays.stream(standardEnvironment.getActiveProfiles()).anyMatch(s -> s.equalsIgnoreCase("init"))) {
            initRedis();
            initMySQL();
        }

    }

    //填充数据到MySQL
    private void initMySQL() {
        //删除表
        jdbcTemplate.execute("DROP TABLE IF EXISTS `r`;");
        //新建表，name字段做了索引
        jdbcTemplate.execute("CREATE TABLE `r` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `data` varchar(2000) NOT NULL,\n" +
                "  `name` varchar(20) NOT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  KEY `name` (`name`) USING BTREE\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");

        //批量插入数据
        String sql = "INSERT INTO `r` (`data`,`name`) VALUES (?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, PAYLOAD);
                preparedStatement.setString(2, "item" + i);
            }

            @Override
            public int getBatchSize() {
                return ROWS;
            }
        });
        log.info("init mysql finished with count {}", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM `r`", Long.class));
    }

    //填充数据到Redis
    private void initRedis() {
        IntStream.rangeClosed(1, ROWS).forEach(i -> stringRedisTemplate.opsForValue().set("item" + i, PAYLOAD));
        log.info("init redis finished with count {}", stringRedisTemplate.keys("item*").size());
    }


    @GetMapping("redis")
    public String redis() {
       return stringRedisTemplate.opsForValue().get("item" + (ThreadLocalRandom.current().nextInt(ROWS) + 1));
    }

    @GetMapping("mysql")
    public String mysql() {
        return  jdbcTemplate.queryForObject("SELECT data FROM `r` WHERE name=?", new Object[]{("item" + (ThreadLocalRandom.current().nextInt(ROWS) + 1))}, String.class);

    }



    @GetMapping("redislike")
    public Set<String> redis2() {
       return  stringRedisTemplate.keys("item71*");
    }


    @GetMapping("mysqllike")
    public List<String> mysql2() {
        return   jdbcTemplate.queryForList("SELECT name FROM `r` WHERE name LIKE 'item71%'", String.class);
    }
}
