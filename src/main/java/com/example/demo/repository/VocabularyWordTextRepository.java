package com.example.demo.repository;

import com.example.demo.domain.entity.VocabularyWordText;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VocabularyWordTextRepository extends JpaRepository<VocabularyWordText, Long> {

  List<VocabularyWordText> findByWord(String word);

  void deleteAllByWord(String word);

  Optional<VocabularyWordText> findByWordAndTextId(String word, Long textId);

  List<VocabularyWordText> findByTextId(Long textId);
}
