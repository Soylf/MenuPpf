package com.example.demo.api.config;

import com.example.demo.model.Constants;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class BotConfig {
    private final String botName = Constants.BOT_NAME.getValue();
    private final String token = Constants.BOT_TOKEN.getValue();
}