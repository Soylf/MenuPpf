package com.example.demo.repository;

import com.example.demo.model.OrderUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderUserRepository extends JpaRepository<OrderUser, Long> {
    void deleteByOrderKey(int orderKey);
}
