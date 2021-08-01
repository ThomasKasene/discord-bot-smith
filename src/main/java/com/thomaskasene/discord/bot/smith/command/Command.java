package com.thomaskasene.discord.bot.smith.command;

import discord4j.core.object.entity.Message;

public interface Command {

    boolean execute(Message message, String command);

}
