package com.example.demo.api.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("src/main/java/com/example/demo/model/Constants.java")
public class BotConfig {
    @Value("${bot_name}")
    String botName;
    @Value("${bot_token}")
    String token;
}
