package com.example.demo.server.service;

import com.example.demo.api.config.BotConfig;
import com.example.demo.model.Item;
import com.example.demo.model.ItemDto;
import com.example.demo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final BotConfig botConfig;
    private final ItemRepository repository;

    @Override
    public void save(Item item) {
        repository.save(item);
    }

    @Override
    public void delete(String name) {
        repository.deleteByName(name);
    }

    @Override
    public List<ItemDto> getAll() {
        return repository.findAll().stream()
                .map(ItemDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public void saveOptionsBot(String botName, String botToken) {
        botConfig.updateBotConfig(botName, botToken);
    }

    @Override
    public List<String> getAllCategory() {
        return repository.findDistinctCategories();
    }
}
