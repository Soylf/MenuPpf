package com.example.demo.repository;

import com.example.demo.model.ApiCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiCredentialRepository extends JpaRepository<ApiCredential, Long>{
    Optional<ApiCredential> findByBankName(String bankName);
}
