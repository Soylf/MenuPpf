package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationsRequestDto {
    private String userName;
    private String age;
    private String relations;
    private String comment;
    private List<ItemDto> itemDtos;
}
