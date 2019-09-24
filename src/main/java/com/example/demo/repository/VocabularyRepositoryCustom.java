package com.example.demo.repository;

import com.example.demo.domain.entity.VocabularyWord;

import java.util.Collection;
import java.util.List;

public interface VocabularyRepositoryCustom {

  List<VocabularyWord> findByWords(Collection<String> words);
}
