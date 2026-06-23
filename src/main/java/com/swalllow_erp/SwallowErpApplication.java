package com.swalllow_erp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication  // @Configuration、@EnableAutoConfiguration、@ComponentScan的组合
@EnableAsync
public class SwallowErpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwallowErpApplication.class, args);
    }
}