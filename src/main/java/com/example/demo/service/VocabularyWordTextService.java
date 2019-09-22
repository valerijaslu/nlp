package com.example.demo.service;

import com.example.demo.domain.entity.VocabularyWordText;

import java.util.List;

public interface VocabularyWordTextService {

  List<VocabularyWordText> saveWordsFromText(List<String> words, Long textId);

}
