package com.example.demo.server.controller;

import com.example.demo.api.TelegramBotService;
import com.example.demo.model.Item;
import com.example.demo.model.ItemDto;
import com.example.demo.server.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Log4j2
public class ItemController {
    private final ItemService service;
    TelegramBotService telegramBotService;

    @PostMapping("/items")
    public ResponseEntity<String> saveItem(
            @RequestParam String name,
            @RequestParam String price,
            @RequestParam String category,
            @RequestParam(required = false) String description,
            @RequestParam String pieces,
            @RequestParam String heft,
            @RequestParam("image") MultipartFile imageFile
    ) throws IOException {

        Item item = new Item();
        item.setName(name);
        item.setPrice(price);
        item.setCategory(category);
        item.setDescription(description);
        item.setHeft(heft);
        item.setPieces(pieces);
        if (imageFile != null && !imageFile.isEmpty()) {
            item.setImage(imageFile.getBytes());
            item.setImageFormat(imageFile.getContentType());
        }

        service.save(item);
        return ResponseEntity.ok("Товар создан");
    }

    @DeleteMapping("/items")
    public ResponseEntity<String> deleteItem(@RequestParam String name) {
        service.delete(name);
        return ResponseEntity.ok("Товар удалён");
    }

    @GetMapping("/items")
    public List<ItemDto> getAllItems() {
        return service.getAll();
    }

    //Бесполезно через service, но хоть красиво ^^
    @PostMapping("/save-options/bot")
    public ResponseEntity<String> saveOptionsBot(@RequestParam String botName,
                                              @RequestParam String botToken) {
        log.info(botToken);
        service.saveOptionsBot(botName, botToken);
        return ResponseEntity.ok("Настройки сохранены!");
    }

    @PostMapping("/save-options/pay-num")
    public ResponseEntity<String> saveOptionsPayNum(@RequestParam String payNum) {
        service.saveOptionsPayNum(payNum);
        return ResponseEntity.ok("Настройки сохранены!");
    }

    @GetMapping("/save-options/pay-num")
    public String saveOptionsPayNum() {
        return service.getOptionsPayNum();
    }

    @PostMapping("/set-items")
    public ResponseEntity<String> setItemsBot(List<Item> items) {
        telegramBotService.setItems(items);
        return ResponseEntity.ok("Все ок");
    }

    @GetMapping("/category")
    public List<String> getAllByCategory() {
        return service.getAllCategory();
    }

}
