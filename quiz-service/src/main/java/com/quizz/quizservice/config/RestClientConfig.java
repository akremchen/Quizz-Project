package com.quizz.quizservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${achievement.service.url:http://achievement-service:8083}")
    private String achievementServiceUrl;

    @Bean
    public RestClient achievementRestClient() {
        return RestClient.builder()
                .baseUrl(achievementServiceUrl)
                .build();
    }
}
