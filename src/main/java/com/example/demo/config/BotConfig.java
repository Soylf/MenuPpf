package com.example.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
public class BotConfig {
    private String botName = "TestBot";
    private String token = "TestBotToken";

    public void updateBotConfig(String botName, String token) {
        this.botName = botName;
        this.token = token;
    }
}