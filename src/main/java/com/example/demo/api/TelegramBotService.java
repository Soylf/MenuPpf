package com.example.demo.api;

import com.example.demo.api.config.BotConfig;
import com.example.demo.model.dto.FeedbackFormDto;
import com.example.demo.model.Item;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramBotService extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    @Setter
    private List<Item> items = new ArrayList<>();
    @Setter
    private FeedbackFormDto feedbackFormDto;

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