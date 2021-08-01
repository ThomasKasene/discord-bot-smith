package com.thomaskasene.discord.bot.smith.selfping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ping")
public class SelfPingController {

    @GetMapping
    public void getPing() {
    }
}
