package com.bread.coalquality.mvc.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Description: httpclient
 * @Author: haoxd
 * @Version: 1.0
 */
@Slf4j
@RequestMapping("/httpclient")
@RestController
public class HttpClientController {

    private static CloseableHttpClient httpClient = null;

    static {
        //当然，也可以把CloseableHttpClient定义为Bean，然后在@PreDestroy标记的方法内close这个HttpClient
        //把 CloseableHttpClient 声明为 static，只创建一次，并且在 JVM 关闭之前通过 addShutdownHook 钩子关闭连接池，在使用的时候直接使用 CloseableHttpClient 即可，无需每次都创建。
        httpClient = HttpClients.custom().setMaxConnPerRoute(1).setMaxConnTotal(1).evictIdleConnections(60L, TimeUnit.SECONDS).build();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                httpClient.close();
            } catch (IOException ignored) {

            }
        }));
    }

    /**
     *
     *
     * 首先，创建一个 CloseableHttpClient，设置使用 PoolingHttpClientConnectionManager 连接池并启用空闲连接驱逐策略，最大空闲时间为 60 秒，然后使用这个连接来请求一个会返回 OK 字符串的服务端接口
     *
     * */
    @GetMapping("wrong1")
    public String wrong1() {
        CloseableHttpClient client = HttpClients.custom()
                .setConnectionManager(new PoolingHttpClientConnectionManager())
                .evictIdleConnections(60L, TimeUnit.SECONDS).build();

        try (CloseableHttpResponse response = client.execute(new HttpGet("http://127.0.0.1:8080/httpclientnotreuse/test"))) {

            return EntityUtils.toString(response.getEntity());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 修复之前按需创建 CloseableHttpClient 的代码，每次用完之后确保连接池可以关闭：
     * */
    @GetMapping("wrong2")
    public String wrong2() {
        try (CloseableHttpClient client = HttpClients.custom()
                .setConnectionManager(new PoolingHttpClientConnectionManager())
                .evictIdleConnections(60L, TimeUnit.SECONDS).build();
             CloseableHttpResponse response = client.execute(new HttpGet("http://127.0.0.1:8080/httpclientnotreuse/test"))) {

            return EntityUtils.toString(response.getEntity());



        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }



    @GetMapping("right")
    public String right() {
        try (CloseableHttpResponse response = httpClient.execute(new HttpGet("http://127.0.0.1:8080/httpclientnotreuse/test"))) {
            return EntityUtils.toString(response.getEntity());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
