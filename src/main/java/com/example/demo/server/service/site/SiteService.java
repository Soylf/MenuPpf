package com.example.demo.server.service.site;

import com.example.demo.model.dto.ItemDto;

import java.util.List;

public interface SiteService {
    List<ItemDto> getAll();
    void saveOptionsBot(String botName, String botToken);
    List<String> getAllCategory();
}
