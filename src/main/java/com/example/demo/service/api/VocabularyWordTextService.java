package com.example.demo.service.api;

import com.example.demo.domain.entity.Text;
import com.example.demo.domain.entity.VocabularyWordText;

import java.util.List;

public interface VocabularyWordTextService {

  List<String> getWordsFromText(Long textId);

  List<VocabularyWordText> saveWordsFromText(List<String> words, Long textId);

  List<Long> findTextIdsByWord(String word);

  void editWord(String oldWord, String newWord, List<Text> mentions);

}
