package com.example.demo.service;

import com.example.demo.domain.entity.VocabularyWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VocabularyService {

  Page<VocabularyWord> getVocabularyWords(Pageable page);
}
