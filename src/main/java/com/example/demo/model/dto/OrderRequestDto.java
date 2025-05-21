package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    private int sum;
    private String name;
    private String userName;
    private String age;
    private String relations;
    private String comment;
    private List<ItemDto> itemsDto;
}
