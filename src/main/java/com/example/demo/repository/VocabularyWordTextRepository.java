package com.example.demo.repository;

import com.example.demo.domain.entity.VocabularyWordText;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VocabularyWordTextRepository extends JpaRepository<VocabularyWordText, Long> {
}
