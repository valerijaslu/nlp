package com.example.demo.service.api;

import com.example.demo.domain.entity.VocabularyWord;
import com.example.demo.domain.model.VocabularyWordCanonical;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface VocabularyService {

  Page<VocabularyWord> getVocabularyWords(Pageable page);

  Page<VocabularyWord> findWordsBySearchFragment(String word, Pageable page);

  void editWord(String oldWord, String newWord);

  Optional<VocabularyWord> findByWord(String word);

  VocabularyWord getWord(String word);

  VocabularyWordCanonical getCanonicalWord(String word);

}
