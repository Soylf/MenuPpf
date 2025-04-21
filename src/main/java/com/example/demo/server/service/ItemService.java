package com.example.demo.server.service;

import com.example.demo.model.Item;

import java.util.List;

public interface ItemService {

    void save(Item item);
    void delete(String name);
    List<Item> getAll();
}
