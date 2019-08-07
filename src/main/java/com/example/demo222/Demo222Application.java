package com.example.demo222;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.demo222.repository") //设置mapper接口的扫描包
@SpringBootApplication
public class Demo222Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo222Application.class, args);
    }
}
