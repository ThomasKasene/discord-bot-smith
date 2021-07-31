package com.thomaskasene.discord.bot.smith.selfping;

import com.thomaskasene.discord.bot.smith.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ScheduledSelfPingConfig {

    private final ApplicationProperties applicationProperties;
    private final RestTemplate restTemplate;

    @Scheduled(fixedDelay = 10_000L)
    public void executeScheduledSelfPing() {
        log.info("Pinging self");
        restTemplate.getForEntity(applicationProperties.getSelfPingUrl(), Void.class);
    }
}
