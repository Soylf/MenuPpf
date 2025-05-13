package com.example.demo.server.controller;

import com.example.demo.api.TelegramBotService;
import com.example.demo.model.Item;
import com.example.demo.model.dto.FeedbackFormDto;
import com.example.demo.model.dto.ItemDto;
import com.example.demo.server.service.site.SiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class SiteController {
    private final SiteService service;
    private final TelegramBotService telegramBotService;

    @GetMapping
    public List<ItemDto> getAllItems() {
        return service.getAll();
    }

    @GetMapping("/category")
    public List<String> getAllCategory() {
        return service.getAllCategory();
    }

    @PostMapping("/save-options/bot")
    public ResponseEntity<String> saveOptionsBot(@RequestParam String botName,
                                                 @RequestParam String botToken) {
        service.saveOptionsBot(botName, botToken);
        return ResponseEntity.ok("Настройки сохранены!");
    }

    @PostMapping("/set-items")
    public ResponseEntity<String> setItemsBot(List<Item> items) {
        telegramBotService.setItems(items);
        return ResponseEntity.ok("Все ок");
    }

    @PostMapping("/feedback-form")
    public ResponseEntity<String> setFeedBackForm( @RequestParam String userName,
                                                   @RequestParam String age,
                                                   @RequestParam String relations,
                                                   @RequestParam String comment) {
        FeedbackFormDto feedbackFormDto = new FeedbackFormDto();
        feedbackFormDto.setUserName(userName);
        feedbackFormDto.setAge(age);
        feedbackFormDto.setRelations(relations);
        feedbackFormDto.setComment(comment);
        telegramBotService.setFeedbackFormDto(feedbackFormDto);
        return ResponseEntity.ok("Все ок");
    }
}
