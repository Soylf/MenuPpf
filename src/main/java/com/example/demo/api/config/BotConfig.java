package com.example.demo.api.config;

import com.example.demo.model.OptionsSite;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class BotConfig {
    private OptionsSite optionsSite = new OptionsSite();
    private final String botName = optionsSite.getBotName();
    private final String token = optionsSite.getBotToken();
}