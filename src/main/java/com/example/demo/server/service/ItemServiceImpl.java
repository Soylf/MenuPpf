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
import java.util.Arrays;
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
    public void save(Item item) throws IOException {
        if(item.getImage() != null) {
            byte[] compressedImage = ImageCompressor.compressImage(item.getImage());
            item.setImage(compressedImage);
        }
        repository.save(item);
    }

    @Override
    public void delete(String name) {
        repository.deleteByName(name);
    }

    @Override
    public List<ItemDto> getAll() {
        return repository.findAll().stream()
                .map(item -> {
                    String imageBase64 = null;
                    if(item.getImage() != null) {
                        try {
                            byte[] decompressedImage = ImageDecompressor.decompress(item.getImage());
                            imageBase64 = Base64.getEncoder().encodeToString(decompressedImage);
                        } catch (IOException ignored) {
                        }
                    }
                    return new ItemDto(item, imageBase64);
                })
                .collect(Collectors.toList());
    }
}
