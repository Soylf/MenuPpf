package com.example.demo.model;

import lombok.*;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class OptionsSite {
    private String botName = "YouBot";
    private String botToken = "YouBotToken";
    private String payNum;
}