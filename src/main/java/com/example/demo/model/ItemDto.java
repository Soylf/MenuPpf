package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDto {
    private String name;
    private String price;
    private String description;
    private String category;
    private String heft;
    private String pieces;
    private String image;

    public ItemDto(Item item, String imageBase64) {
        this.name = item.getName();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.category = item.getCategory();
        this.pieces = item.getPieces();
        this.heft = item.getHeft();
        if (item.getImage() != null) {
            this.image = imageBase64;
        } else {
            this.image = null;
        }
    }
}