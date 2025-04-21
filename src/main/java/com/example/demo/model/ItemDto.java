package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Data
@NoArgsConstructor
public class ItemDto {
    private String name;
    private String price;
    private String description;
    private String category;
    private String image;

    public ItemDto(Item item) {
        this.name = item.getName();
        this.price = item.getPrice();
        this.description = item.getDescription();
        this.category = item.getCategory();
        this.image = Base64.getEncoder().encodeToString(item.getImage());
    }

}