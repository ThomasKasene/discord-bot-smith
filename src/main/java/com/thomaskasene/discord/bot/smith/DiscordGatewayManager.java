package com.thomaskasene.discord.bot.smith;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

@Slf4j
@Configuration
public class DiscordGatewayManager implements DisposableBean {

    private final GatewayDiscordClient gatewayDiscordClient;

    public DiscordGatewayManager(ApplicationProperties applicationProperties) {
        DiscordClient discordClient = DiscordClient.create(applicationProperties.getDiscordBotToken());
        gatewayDiscordClient = discordClient.login().block();

        Pattern pattern = Pattern.compile("^(smith, )?(.+)(, smith[\\\\?]?)?$", CASE_INSENSITIVE);

        gatewayDiscordClient.on(MessageCreateEvent.class).subscribe(messageCreateEvent -> {
            Message message = messageCreateEvent.getMessage();

            log.info("Received a message: {}", message.getContent());

            String command = pattern.matcher(message.getContent()).group(2);

            log.info("Resolved the command {}", command);

            if (command != null) {
                message.getChannel().subscribe(channel -> {
                    channel.createMessage("You said \"" + command + "\"");
                });
            }
        });
    }

    @Override
    public void destroy() {
        gatewayDiscordClient.onDisconnect().block();
    }
}
