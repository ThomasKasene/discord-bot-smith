package com.thomaskasene.discord.bot.smith;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

@Slf4j
@Configuration
public class DiscordGatewayManager implements DisposableBean {

    private final GatewayDiscordClient gatewayDiscordClient;
    private final User selfUser;

    public DiscordGatewayManager(ApplicationProperties applicationProperties) {
        DiscordClient discordClient = DiscordClient.create(applicationProperties.getDiscordBotToken());
        gatewayDiscordClient = discordClient.login().block();
        selfUser = gatewayDiscordClient.getSelf().block();

        Pattern pattern = Pattern.compile("^(smith, )?(.+)(, smith[\\\\?]?)?$", CASE_INSENSITIVE);

        gatewayDiscordClient.on(MessageCreateEvent.class).subscribe(messageCreateEvent -> {
            Message message = messageCreateEvent.getMessage();

            if (message.getAuthor().map(user -> user.equals(selfUser)).orElse(false)) {
                return; // This message was posted by us
            }

            log.info("Received a message: {}", message.getContent());

            Matcher matcher = pattern.matcher(message.getContent());

            if (!matcher.find()) {
                return; // This message isn't for us
            }

            String command = matcher.group(2);

            log.info("Resolved the command {}", command);

            if (command != null) {
                message.getChannel().subscribe(channel -> {
                    channel.createMessage("You said \"" + command + "\"").subscribe();
                });
            }
        });
    }

    @Override
    public void destroy() {
        gatewayDiscordClient.onDisconnect().block();
    }
}
