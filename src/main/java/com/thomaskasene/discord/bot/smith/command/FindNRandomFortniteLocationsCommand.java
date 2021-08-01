package com.thomaskasene.discord.bot.smith.command;

import com.thomaskasene.discord.bot.smith.fortniteapi.FortniteApiService;
import discord4j.core.object.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

@Component
@RequiredArgsConstructor
public class FindNRandomFortniteLocationsCommand implements Command {

    private final Pattern applicabilityPattern = Pattern.compile(
            "^find( us)? (a|some|[0-9]+)( random)?( fortnite)? location[s]?$",
            CASE_INSENSITIVE);

    private final Random randomGenerator = new Random();

    private final FortniteApiService fortniteApiService;

    @Override
    public boolean execute(Message message, String command) {

        Matcher matcher = applicabilityPattern.matcher(command);

        if (!matcher.find()) {
            return false;
        }

        List<String> namedLocations = new LinkedList<>(fortniteApiService.findNamedLocationNames());

        String amountOfLocations = matcher.group(2);

        int desiredLocationCount;

        if (amountOfLocations.equalsIgnoreCase("a") || amountOfLocations.equals("1")) {
            desiredLocationCount = 1;
        } else if (amountOfLocations.equalsIgnoreCase("some")) {
            desiredLocationCount = 3;
        } else {
            desiredLocationCount = Math.min(Integer.parseInt(amountOfLocations), namedLocations.size());
        }

        Set<String> chosenLocations = new HashSet<>(desiredLocationCount);

        while (chosenLocations.size() < desiredLocationCount) {
            chosenLocations.add(namedLocations.remove(randomGenerator.nextInt(namedLocations.size())));
        }

        String newLineDelimitedLocations = String.join("\n", chosenLocations);

        message.getChannel().subscribe(messageChannel -> messageChannel.createMessage(newLineDelimitedLocations).subscribe());

        return true;
    }
}
