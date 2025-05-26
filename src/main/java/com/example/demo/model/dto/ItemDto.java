package com.example.demo.model.dto;

import com.example.demo.model.Item;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String price;
    private String description;
    private String category;
    private String heft;
    private String image;

    private int quantity;
    private String pieces;

    public ItemDto(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.category = item.getCategory();
        this.heft = item.getHeft();
        this.image = item.getImage();
    }
}