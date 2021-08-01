package com.thomaskasene.discord.bot.smith;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {

    private String selfPingUrl;

    private String discordBotToken;

    private FortniteApi fortniteApi = new FortniteApi();

    @Data
    public static class FortniteApi {

        private String url;

        private String token;

    }
}
