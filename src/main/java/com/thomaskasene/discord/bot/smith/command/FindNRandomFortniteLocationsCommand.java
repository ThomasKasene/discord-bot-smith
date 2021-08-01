package com.thomaskasene.discord.bot.smith.command;

import com.thomaskasene.discord.bot.smith.fortniteapi.FortniteApiService;
import discord4j.core.object.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Character.toUpperCase;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.stream.Collectors.joining;

@Component
@RequiredArgsConstructor
public class FindNRandomFortniteLocationsCommand implements Command {

    private final Pattern applicabilityPattern = Pattern.compile(
            "^(?:pick|choose|find(?: us| me)?|give (?:us|me)) (a|some|[0-9]+)(?: random)?(?: fortnite)? location[s]?$",
            CASE_INSENSITIVE);

    private final FortniteApiService fortniteApiService;

    @Override
    public boolean execute(Message message, String command) {

        Matcher matcher = applicabilityPattern.matcher(command);

        if (matcher.find()) {
            List<String> namedLocations = fortniteApiService.findNamedLocationNames();

            int desiredLocationCount = translateToLocationCount(matcher.group(1));
            int limitedLocationCount = Math.min(desiredLocationCount, namedLocations.size());

            List<String> selectedLocations = selectRandomNLocations(namedLocations, limitedLocationCount);

            String resultMessage = createResultMessage(selectedLocations);

            message.getChannel().subscribe(messageChannel -> messageChannel.createMessage(resultMessage).subscribe());

            return true;
        }

        return false;
    }

    private int translateToLocationCount(String command) {
        if (command.equalsIgnoreCase("a") || command.equals("1")) {
            return 1;
        } else if (command.equalsIgnoreCase("some")) {
            return 3;
        } else {
            return Integer.parseInt(command);
        }
    }

    private List<String> selectRandomNLocations(List<String> availableLocations, int count) {
        List<String> shuffledLocations = new ArrayList<>(availableLocations);
        Collections.shuffle(shuffledLocations);
        return shuffledLocations.subList(0, count);
    }

    private String createResultMessage(List<String> selectedLocations) {
        return selectedLocations.stream()
                .map(locationName -> {
                    String[] nameParts = locationName.split(" ");
                    return Stream.of(nameParts)
                            .map(namePart -> toUpperCase(namePart.charAt(0)) + namePart.substring(1).toLowerCase())
                            .collect(joining(" "));
                })
                .collect(joining("\n"));
    }
}
