package com.example.demo.controller;

import com.example.demo.domain.entity.VocabularyWord;
import com.example.demo.service.VocabularyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("vocabulary")
public class VocabularyController {

  @Autowired
  private VocabularyService vocabularyService;

  @GetMapping
  public Page<VocabularyWord> vocabulary(Pageable page) {
    Page<VocabularyWord> vocabularyWords = vocabularyService.getVocabularyWords(page);
    return vocabularyWords;
  }
}
