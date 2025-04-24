package com.example.demo.server.service;

import com.example.demo.model.Item;
import com.example.demo.model.ItemDto;
import com.example.demo.model.compessor.ImageCompressor;
import com.example.demo.model.compessor.ImageDecompressor;
import com.example.demo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private static final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);
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

}
