package com.example.demo.server.controller;

import com.example.demo.model.dto.FeedbackFormDto;
import com.example.demo.model.dto.ItemDto;
import com.example.demo.model.dto.OrderRequestDto;
import com.example.demo.model.dto.RelationsRequestDto;
import com.example.demo.server.service.site.SiteService;
import com.example.demo.server.service.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SiteController {
    private final SiteService service;
    private final TelegramService telegramService;

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
    public ResponseEntity<String> createOrder(@RequestBody OrderRequestDto request) {
        setFeedBackForm(
                request.getUserName(),
                request.getAge(),
                request.getRelations(),
                request.getComment(),
                "ОПЛАТА",
                request.getSum(),
                request.getItemDtos()
        );
        String qr = service.generateQr(request.getSum(), request.getName());
        return ResponseEntity.ok(qr);
    }

    @PostMapping("/order/relations")
    public void relationsInfo(@RequestBody RelationsRequestDto request) {
        setFeedBackForm(
                request.getUserName(),
                request.getAge(),
                request.getRelations(),
                request.getComment(),
                "СВЯЗЬ",
                request.getSum(),
                request.getItemDtos()
        );
    }

    private void setFeedBackForm(String userName,
                                 String age,
                                 String relations,
                                 String comment,
                                 String status,
                                 int sum,
                                 List<ItemDto> itemDtos) {
        FeedbackFormDto feedbackFormDto = new FeedbackFormDto();
        feedbackFormDto.setUserName(userName);
        feedbackFormDto.setAge(age);
        feedbackFormDto.setRelations(relations);
        feedbackFormDto.setComment(comment);
        feedbackFormDto.setStatus(status);
        feedbackFormDto.setSum(sum);

        telegramService.setFeedbackFormDto(feedbackFormDto);
        telegramService.setItems(itemDtos);
        telegramService.saveMessage();
    }
}
