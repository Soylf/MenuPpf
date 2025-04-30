package com.example.demo.server.service;

import com.example.demo.model.Item;
import com.example.demo.model.ItemDto;

import java.util.List;

public interface ItemService {

    void save(Item item);
    void delete(String name);
    List<ItemDto> getAll();
    void saveOptionsPayNum(String payNum);
    void saveOptionsBot(String botName, String botToken);
    String getOptionsPayNum();
}
