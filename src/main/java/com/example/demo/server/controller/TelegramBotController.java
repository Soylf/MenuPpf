package com.example.demo.server.controller;

import com.example.demo.config.BotConfig;
import com.example.demo.server.service.telegram.TelegramService;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Component
@RequiredArgsConstructor
public class TelegramBotController extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final TelegramService service;
    private final Set<Long> chatIds = new HashSet<>();
    private final Map<Long, Boolean> awaitingDeleteInput = new HashMap<>();


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
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();
            chatIds.add(chatId);

            if ("📋 Все заказы".equals(text)) {
                sendMessageWithKeyboard(chatId, buildAllOrdersMessage());
                return;
            }

            if ("🗑 Удалить заказ".equals(text)) {
                awaitingDeleteInput.put(chatId, true);
                sendMessageWithKeyboard(chatId, "Пожалуйста, введите номер заказа, который хотите удалить:");
                return;
            }

            if (awaitingDeleteInput.getOrDefault(chatId, false)) {
                try {
                    int keyToDelete = Integer.parseInt(text);
                    service.deleteOrderUser(keyToDelete);
                    sendMessageWithKeyboard(chatId, "✅ Заказ №" + keyToDelete + " был удалён.");
                } catch (NumberFormatException e) {
                    sendMessageWithKeyboard(chatId, "❌ Пожалуйста, введите корректный номер заказа.");
                }
                awaitingDeleteInput.put(chatId, false);
                return;
            }

            sendMessageWithKeyboard(chatId, "Выберите действие на клавиатуре ниже.");
        }
    }

    private void sendMessageWithKeyboard(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(text);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);

        KeyboardRow row1 = new KeyboardRow();
        row1.add("📋 Все заказы");
        row1.add("🗑 Удалить заказ");

        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(row1);

        keyboardMarkup.setKeyboard(rows);
        sendMessage.setReplyMarkup(keyboardMarkup);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String buildAllOrdersMessage() {
        List<String> messages = service.getMessages();
        if (messages.isEmpty()) {
            return "Пока нет сохранённых заказов.";
        }
        StringBuilder allMessages = new StringBuilder("Все заказы:\n\n");
        for (String message : messages) {
            allMessages.append(message).append("\n");
        }
        return allMessages.toString();
    }
}