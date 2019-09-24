package com.example.demo.service;

import com.example.demo.domain.entity.VocabularyWord;
import com.example.demo.repository.VocabularyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VocabularyServiceImpl implements VocabularyService {

  @Autowired
  private VocabularyRepository vocabularyRepository;

  @Override
  public Page<VocabularyWord> getVocabularyWords(Pageable page) {
    return vocabularyRepository.findAll(page);
  }

  @Override
  public Page<VocabularyWord> findWordsBySearchFragment(String word, Pageable page) {
    return vocabularyRepository.findByWordLike("%" + word + "%", page);
  }
}
