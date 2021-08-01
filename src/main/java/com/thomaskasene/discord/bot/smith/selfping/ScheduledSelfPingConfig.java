package com.thomaskasene.discord.bot.smith.selfping;

import com.thomaskasene.discord.bot.smith.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
public class ScheduledSelfPingConfig {

    private final RestTemplate restTemplate;

    public ScheduledSelfPingConfig(ApplicationProperties applicationProperties, RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .rootUri(applicationProperties.getSelfPingUrl())
                .build();
    }

    @Scheduled(fixedDelay = 10 * 60 * 1000)
    public void executeScheduledSelfPing() {
        log.info("Pinging self");
        restTemplate.getForEntity("/api/ping", Void.class);
    }
}
