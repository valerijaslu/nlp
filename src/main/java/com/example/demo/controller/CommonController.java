package com.example.demo.controller;

import com.example.demo.repository.AnnotatedTextRepository;
import com.example.demo.repository.TextRepository;
import com.example.demo.repository.VocabularyRepository;
import com.example.demo.repository.VocabularyWordTextRepository;
import com.example.demo.repository.WordCanonicalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("common")
public class CommonController {

  @Autowired
  private TextRepository textRepository;

  @Autowired
  private VocabularyRepository vocabularyRepository;

  @Autowired
  private VocabularyWordTextRepository vocabularyWordTextRepository;

  @Autowired
  private WordCanonicalRepository wordCanonicalRepository;

  @Autowired
  private AnnotatedTextRepository annotatedTextRepository;

  @DeleteMapping("deleteAll")
  public void clearAll() {
    textRepository.deleteAll();
    vocabularyRepository.deleteAll();
    vocabularyWordTextRepository.deleteAll();
    wordCanonicalRepository.deleteAll();
    annotatedTextRepository.deleteAll();
  }
}
