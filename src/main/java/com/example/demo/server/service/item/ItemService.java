package com.example.demo.server.service.item;

import com.example.demo.model.Item;

public interface ItemService {

    void save(Item item);
    void delete(String name);
}
