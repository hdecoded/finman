package com.hdecoded.finman.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "finman")
public class FinmanProperties {

    private final Frontend frontend = new Frontend();

    @Getter
    @Setter
    public static class Frontend {

        private String url;
    }
}
