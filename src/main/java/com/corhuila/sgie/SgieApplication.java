package com.corhuila.sgie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SgieApplication {

    public static void main(String[] args) {
        SpringApplication.run(SgieApplication.class, args);
    }

}
