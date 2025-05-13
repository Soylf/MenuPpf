package com.example.demo.server.service.site;

import com.example.demo.api.config.BotConfig;
import com.example.demo.model.dto.ItemDto;
import com.example.demo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SiteServiceImpl implements SiteService{
    private final BotConfig botConfig;
    private final ItemRepository repository;

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
