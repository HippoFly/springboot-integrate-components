package com.example.springintegratecaffeine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SpringIntegrateCaffeineApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringIntegrateCaffeineApplication.class, args);
    }

}
