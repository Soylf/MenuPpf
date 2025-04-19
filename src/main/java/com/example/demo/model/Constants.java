package com.example.demo.model;

import lombok.Getter;

@Getter
public enum Constants {
    BOT_NAME("YourBotName"),
    BOT_TOKEN("YourBotToken");

    private final String value;

    Constants(String value) {
        this.value = value;
    }

}