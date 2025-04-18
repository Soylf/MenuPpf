package com.example.demo.service;

import com.example.demo.model.Item;

public interface ItemService {

    void save(Item item);
    void delete(Item item);
    Item get(String name);
}
