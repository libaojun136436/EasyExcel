package com.example.gomeexcel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.gomeexcel.dao")
public class GomeexcelApplication {

    public static void main(String[] args) {
        SpringApplication.run(GomeexcelApplication.class, args);
    }
    
}
