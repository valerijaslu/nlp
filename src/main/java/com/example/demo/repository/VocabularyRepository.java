package com.example.demo.repository;

import com.example.demo.domain.entity.VocabularyWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface VocabularyRepository extends JpaRepository<VocabularyWord, String> {

  List<VocabularyWord> findAllByWordIn(Set<String> words);
}
