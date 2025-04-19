package com.example.demo.server.service;

import com.example.demo.model.Item;
import com.example.demo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private ItemRepository repository;

    @Override
    public void save(Item item) {
        repository.save(item);
    }

    @Override
    public void delete(String name) {
        repository.deleteByName(name);
    }

    @Override
    public Item get(String name) {
        return repository.getByName(name);
    }
}
