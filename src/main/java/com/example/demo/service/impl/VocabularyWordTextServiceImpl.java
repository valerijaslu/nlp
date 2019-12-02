package com.example.demo.service.impl;

import com.example.demo.domain.entity.Text;
import com.example.demo.domain.entity.VocabularyWordText;
import com.example.demo.repository.VocabularyWordTextRepository;
import com.example.demo.service.api.VocabularyWordTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VocabularyWordTextServiceImpl implements VocabularyWordTextService {

  @Autowired
  private VocabularyWordTextRepository vocabularyWordTextRepository;

  @Override
  public List<String> getWordsFromText(Long textId) {
    return vocabularyWordTextRepository.findByTextId(textId).stream()
      .map(VocabularyWordText::getWord)
      .collect(Collectors.toList());
  }

  @Override
  public List<VocabularyWordText> saveWordsFromText(List<String> words, Long textId) {
    List<VocabularyWordText> vocabularyWordTextList = words.stream()
      .map(word -> VocabularyWordText.builder()
        .word(word).textId(textId).build())
      .collect(Collectors.toList());
    return vocabularyWordTextRepository.saveAll(vocabularyWordTextList);
  }

  @Override
  public List<Long> findTextIdsByWord(String word) {
    return vocabularyWordTextRepository.findByWord(word).stream()
      .map(VocabularyWordText::getTextId).collect(Collectors.toList());
  }

  @Transactional
  @Override
  public void editWord(String oldWord, String newWord, List<Text> mentions) {
    vocabularyWordTextRepository.deleteAllByWord(oldWord);
    mentions.forEach(text -> {
      Optional<VocabularyWordText> vocabularyWordTextOptional = vocabularyWordTextRepository.findByWordAndTextId(newWord, text.getId());
      if (!vocabularyWordTextOptional.isPresent()) {
        vocabularyWordTextRepository.save(VocabularyWordText.builder()
          .textId(text.getId()).word(newWord).build());
      }
    });
  }

}
