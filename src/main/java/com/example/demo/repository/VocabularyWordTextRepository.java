package com.example.demo.repository;

import com.example.demo.domain.entity.VocabularyWordText;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VocabularyWordTextRepository extends JpaRepository<VocabularyWordText, Long> {

  List<VocabularyWordText> findByWord(String word);
}
