package com.thomaskasene.discord.bot.smith.fortniteapi;

import com.thomaskasene.discord.bot.smith.ApplicationProperties;
import lombok.Data;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class FortniteApiService {

    private final RestTemplate restTemplate;

    public FortniteApiService(ApplicationProperties applicationProperties, RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                .rootUri(applicationProperties.getFortniteApi().getUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, applicationProperties.getFortniteApi().getToken())
                .build();
    }

    public List<String> findNamedLocationNames() {
        return restTemplate.getForEntity("/v2/game/poi?lang=en", GetGamePoiResponse.class).getBody().getList().stream()
                .map(Poi::getName)
                .collect(toList());
    }

    @Data
    public static class GetGamePoiResponse {
        private List<Poi> list;
    }

    @Data
    public static class Poi {
        private String name;
    }
}
