package com.example.demo.server.service;

import com.example.demo.model.Item;
import com.example.demo.model.ItemDto;

import java.io.IOException;
import java.util.List;

public interface ItemService {

    void save(Item item) throws IOException;
    void delete(String name);
    List<ItemDto> getAll();
}
