package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String price;
    private String name;
    private String category;
    private String description;
    private String heft;
    private String pieces;
    @Lob
    private byte[] image;
    private String imageFormat;

    public String getImageFormat() {
        if (image == null) {
            return null;
        }
        if (imageFormat == null) {
            imageFormat = "image/jpeg";
        }
        return imageFormat;
    }
}
