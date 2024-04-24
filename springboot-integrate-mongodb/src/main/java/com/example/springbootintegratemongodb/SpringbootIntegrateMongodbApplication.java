package com.example.springbootintegratemongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class SpringbootIntegrateMongodbApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootIntegrateMongodbApplication.class, args);
    }

}
