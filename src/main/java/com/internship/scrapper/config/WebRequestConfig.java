package com.internship.scrapper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.web-request")
public class WebRequestConfig {

    private int pageSize;
    private String baseUrl;
    private String apiPath;
}