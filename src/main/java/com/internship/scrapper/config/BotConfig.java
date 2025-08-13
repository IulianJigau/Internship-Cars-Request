package com.internship.scrapper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.bots")
public class BotConfig {

    private String token;
    private String name;
    private String path;
}