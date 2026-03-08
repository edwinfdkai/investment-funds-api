package com.test.bank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class AwsNotificationConfig {

    private final Region region = Region.US_EAST_2;

    @Bean
    public SnsClient snsClient() {
        return SnsClient.builder()
                .region(region)
                .build();
    }

    @Bean
    public SesClient sesClient() {
        return SesClient.builder()
                .region(region)
                .build();
    }
}