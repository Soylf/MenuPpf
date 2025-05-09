package com.example.demo.repository;


import com.example.demo.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {
    void deleteByName(String name);

    @Query("SELECT DISTINCT i.category FROM Item i")
    List<String> findDistinctCategories();
}
