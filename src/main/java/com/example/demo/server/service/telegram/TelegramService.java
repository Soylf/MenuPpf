package com.example.demo.server.service.telegram;


import com.example.demo.model.Item;
import com.example.demo.model.dto.FeedbackFormDto;
import com.example.demo.model.dto.ItemDto;

import java.util.List;

public interface TelegramService {
    void deleteOrderUser(int key);
    List<String> getMessages();
    void saveMessage();
    void setFeedbackFormDto(FeedbackFormDto feedbackFormDto);
    void setItems(List<ItemDto> items);
}
