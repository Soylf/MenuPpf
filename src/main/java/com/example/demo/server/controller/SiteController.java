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
@RequiredArgsConstructor
public class SiteController {
    private final SiteService service;
    private final TelegramBotService telegramBotService;

    @GetMapping("/items")
    public List<ItemDto> getAllItems() {
        return service.getAll();
    }

    @GetMapping("/items/category")
    public List<String> getAllCategory() {
        return service.getAllCategory();
    }

    @PostMapping("/items/save-options/bot")
    public ResponseEntity<String> saveOptionsBot(@RequestParam String botName,
                                                 @RequestParam String botToken) {
        service.saveOptionsBot(botName, botToken);
        return ResponseEntity.ok("Настройки сохранены!");
    }

    @PostMapping("/items/set-items")
    public ResponseEntity<String> setItemsBot(List<Item> items) {
        telegramBotService.setItems(items);
        return ResponseEntity.ok("Все ок");
    }

    @PostMapping("/items/set-bank")
    public ResponseEntity<String> setBank(@RequestParam String client_id,
                                          @RequestParam String client_secret,
                                          @RequestParam String id_name) {
        service.setBank(client_id, client_secret, id_name);
        System.out.println(client_id);
        System.out.println(id_name);
        return ResponseEntity.ok("Все ок");
    }


    @PostMapping("/items/feedback-form")
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

    @PostMapping("/order/create")
    public String createOrder(@RequestParam int sum,
                              @RequestParam String name) {
        return service.generateQr(sum, name);
    }
}
