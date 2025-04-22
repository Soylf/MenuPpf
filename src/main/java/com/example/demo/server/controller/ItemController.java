package com.example.demo.server.controller;

import com.example.demo.model.Item;
import com.example.demo.model.ItemDto;
import com.example.demo.server.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @PostMapping
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
        item.setImage(imageFile.getBytes());

        service.save(item);
        return ResponseEntity.ok("Товар создан");
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteItem(@RequestParam String name) {
        service.delete(name);
        return ResponseEntity.ok("Товар удалён");
    }

    @GetMapping
    public List<ItemDto> getAllItems() {
        return service.getAll();
    }
}
