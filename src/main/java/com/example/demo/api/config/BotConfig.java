package com.example.demo.api.config;

import com.example.demo.api.TelegramBotService;
import com.example.demo.model.OptionsSite;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class BotConfig {

    private final OptionsSite optionsSite;
    private final TelegramBotsApi telegramBotsApi;

    @Getter
    private TelegramBotService botInstance;

    @PostConstruct
    public void initBot() throws TelegramApiException {
        registerBot();
    }

    private void registerBot() throws TelegramApiException {
        botInstance = new TelegramBotService(
                optionsSite.getBotName(),
                optionsSite.getBotToken()
        );
        telegramBotsApi.registerBot(botInstance);
        System.out.println("✅ Бот зарегистрирован: " + botInstance.getBotUsername());
    }

    public void reloadBot() throws TelegramApiException {
        registerBot();
        System.out.println("♻️ Бот переинициализирован");
    }
}