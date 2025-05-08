package com.example.demo.api;

import com.example.demo.api.config.BotConfig;
import com.example.demo.model.Item;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBotService extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    @Setter
    private List<Item> items = new ArrayList<>();

    public TelegramBotService(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

    }
}