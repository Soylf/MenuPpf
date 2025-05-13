package com.example.demo.model.dto;

import com.example.demo.model.Item;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String price;
    private String description;
    private String category;
    private String heft;
    private String pieces;
    private String image;

    public ItemDto(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.category = item.getCategory();
        this.pieces = item.getPieces();
        this.heft = item.getHeft();
        this.image = item.getImage();
    }
}