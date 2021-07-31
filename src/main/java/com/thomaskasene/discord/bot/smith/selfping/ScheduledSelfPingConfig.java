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

    private final ApplicationProperties applicationProperties;
    private final RestTemplate restTemplate;

    public ScheduledSelfPingConfig(ApplicationProperties applicationProperties, RestTemplateBuilder restTemplateBuilder) {
        this.applicationProperties = applicationProperties;
        this.restTemplate = restTemplateBuilder.build();
    }

    @Scheduled(fixedDelay = 10_000L)
    public void executeScheduledSelfPing() {
        log.info("Pinging self");
        restTemplate.getForEntity(applicationProperties.getSelfPingUrl(), Void.class);
    }
}
