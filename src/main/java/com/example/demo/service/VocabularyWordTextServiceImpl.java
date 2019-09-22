package com.example.demo.service;

import com.example.demo.domain.entity.VocabularyWordText;
import com.example.demo.repository.VocabularyWordTextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VocabularyWordTextServiceImpl implements VocabularyWordTextService {

  @Autowired
  private VocabularyWordTextRepository vocabularyWordTextRepository;

  @Override
  public List<VocabularyWordText> saveWordsFromText(List<String> words, Long textId) {
    List<VocabularyWordText> vocabularyWordTextList = words.stream()
      .map(word -> VocabularyWordText.builder()
        .word(word)
        .textId(textId).build())
      .collect(Collectors.toList());
    return vocabularyWordTextRepository.saveAll(vocabularyWordTextList);
  }

}
