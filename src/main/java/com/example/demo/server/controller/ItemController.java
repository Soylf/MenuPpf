package com.example.demo.server.controller;

import com.example.demo.api.TelegramBotService;
import com.example.demo.model.Item;
import com.example.demo.model.ItemDto;
import com.example.demo.server.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Log4j2
public class ItemController {
    @Value("${app.image.storage-path}")
    private String absoluteImagesFolder;
    private final ItemService service;
    TelegramBotService telegramBotService;

    @RequestMapping("/items")
    public ResponseEntity<String> saveItem(
            @RequestParam String name,
            @RequestParam String price,
            @RequestParam String category,
            @RequestParam(required = false) String description,
            @RequestParam String pieces,
            @RequestParam String heft,
            @RequestParam("image") MultipartFile imageFile
    ) throws IOException {
        String fileName = System.currentTimeMillis() + "-" + imageFile.getOriginalFilename();
        File destinationFile = new File(absoluteImagesFolder, fileName);
        File parentDir = destinationFile.getParentFile();

        if (!parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                throw new IOException("Не удалось создать директорию: " + parentDir.getAbsolutePath());
            }
        }

        imageFile.transferTo(destinationFile);
        String imageUrl = "/imgIns/itemImg/" + fileName;

        Item item = new Item();
        item.setName(name);
        item.setPrice(price);
        item.setCategory(category);
        item.setDescription(description);
        item.setHeft(heft);
        item.setPieces(pieces);
        item.setImage(imageUrl);

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

    @GetMapping("/category")
    public List<String> getAllCategory() {
        return service.getAllCategory();
    }

    //Бесполезно через service, но хоть красиво ^^
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
}
