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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class SiteController {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
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
                                          @RequestParam String id_name,
                                          @RequestParam String member_id,
                                          @RequestParam String id_qr) {
        service.setBank(client_id, client_secret, id_name, member_id, id_qr);
        return ResponseEntity.ok("Все ок");
    }


    @PostMapping("/order/create")
    public ResponseEntity<String> createOrder(@RequestParam int sum,
                                              @RequestParam String name,
                                              @RequestParam String userName,
                                              @RequestParam String age,
                                              @RequestParam String relations,
                                              @RequestParam String comment) {
        System.out.println("fff");
        setFeedBackForm(userName, age, relations, comment,"ОПЛАТА");
        String qr = service.generateQr(sum, name);
        return ResponseEntity.ok(qr);
    }

    @PostMapping("/order/relations")
    public void relationsInfo(@RequestParam String userName,
                              @RequestParam String age,
                              @RequestParam String relations,
                              @RequestParam String comment){
        System.out.println("ff");
        setFeedBackForm(userName, age, relations, comment, "СВЯЗЬ");
    }

    private void setFeedBackForm(String userName,
                                 String age,
                                 String relations,
                                 String comment,
                                 String status) {
        FeedbackFormDto feedbackFormDto = new FeedbackFormDto();
        feedbackFormDto.setUserName(userName);
        feedbackFormDto.setAge(age);
        feedbackFormDto.setRelations(relations);
        feedbackFormDto.setComment(comment);
        feedbackFormDto.setStatus(status);

        // минута
        scheduler.schedule(() -> {
            telegramBotService.setFeedbackFormDto(feedbackFormDto);
        }, 1, TimeUnit.MINUTES);
    }
}