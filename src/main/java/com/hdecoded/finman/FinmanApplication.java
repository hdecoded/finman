package com.hdecoded.finman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ConfigurationPropertiesScan
public class FinmanApplication {

    static void main(String[] args) {
        SpringApplication.run(FinmanApplication.class, args);
    }

}
