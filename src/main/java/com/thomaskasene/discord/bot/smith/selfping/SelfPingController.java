package com.thomaskasene.discord.bot.smith.selfping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/ping")
public class SelfPingController {

    @GetMapping
    public void getPing(@RequestHeader("Host") String requestedHost) {
        log.info("Received ping (sent to {})", requestedHost);
    }
}
