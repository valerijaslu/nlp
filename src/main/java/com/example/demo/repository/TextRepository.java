package com.example.demo.repository;

import com.example.demo.domain.entity.Text;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextRepository extends JpaRepository<Text, Long> {
}
