package com.contentmunch.blog.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "muncher.blog")
public class BlogConfig {

    private String clientId;
    private String clientSecret;
    private String refreshToken;
    private String tokenServer;
    private String blogId;

}
