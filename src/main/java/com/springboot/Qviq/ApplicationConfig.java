package com.springboot.Qviq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ApplicationConfig {

    @Autowired
    InfoRepository messageRepository;

    public static void main(String... args) {
        SpringApplication.run(ApplicationConfig.class, args);
    }

    public void run(String... args) throws Exception {
        //Code to run at application startup
    }
}