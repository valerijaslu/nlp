package com.example.demo.repository;

import com.example.demo.domain.entity.VocabularyWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface VocabularyRepositoryCustom {
  List<VocabularyWord> findByWords(Collection<String> words);

  Page<VocabularyWord> findByWords(Collection<String> words, Pageable pageable);

}
