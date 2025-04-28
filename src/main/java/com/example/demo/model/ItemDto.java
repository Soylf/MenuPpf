package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

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
    private String imageMimeType;

    public ItemDto(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.category = item.getCategory();
        this.pieces = item.getPieces();
        this.heft = item.getHeft();

        if (item.getImage() != null && item.getImageFormat() != null) {
            String base64 = Base64.getEncoder().encodeToString(item.getImage());
            this.image = "data:" + item.getImageFormat() + ";base64," + base64;
            this.imageMimeType = item.getImageFormat();
        } else {
            this.image = null;
            this.imageMimeType = null;
        }
    }
}