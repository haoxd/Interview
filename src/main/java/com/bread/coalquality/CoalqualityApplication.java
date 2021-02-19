package com.bread.coalquality;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description:启动类
 * @Author: haoxd
 * @Version: 1.0
 */
@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class CoalqualityApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CoalqualityApplication.class);
        app.run(args);

    }

}
