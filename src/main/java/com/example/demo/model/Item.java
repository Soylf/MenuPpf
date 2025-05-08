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
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String price;
    private String name;
    private String description;
    private String category;
    private String pieces;
    private String heft;
    @Lob
    private byte[] image;
    private String imageFormat;

    public String getImageFormat() {
        return (imageFormat != null) ? imageFormat : "image/jpeg";
    }

}
