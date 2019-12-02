package com.example.demo.repository;

import com.example.demo.domain.entity.AnnotatedText;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnotatedTextRepository extends JpaRepository<AnnotatedText, Long> {
}
