package com.example.demo.service.impl;

import com.example.demo.domain.entity.VocabularyWord;
import com.example.demo.domain.entity.WordCanonical;
import com.example.demo.domain.model.VocabularyWordCanonical;
import com.example.demo.repository.VocabularyRepository;
import com.example.demo.repository.WordCanonicalRepository;
import com.example.demo.service.api.VocabularyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VocabularyServiceImpl implements VocabularyService {

  @Autowired
  private VocabularyRepository vocabularyRepository;

  @Autowired
  private WordCanonicalRepository wordCanonicalRepository;

  @Override
  public Page<VocabularyWord> getVocabularyWords(Pageable page) {
    return vocabularyRepository.findAll(page);
  }

  @Override
  public Page<VocabularyWord> findWordsBySearchFragment(String word, Pageable page) {
    return vocabularyRepository.findByWordLike("%" + word + "%", page);
  }

  @Override
  public void editWord(String oldWord, String newWord) {
    Optional<VocabularyWord> oldVocabularyWordOptional = vocabularyRepository.findById(oldWord);
    if (oldVocabularyWordOptional.isPresent()) {
      VocabularyWord oldVocabularyWord = oldVocabularyWordOptional.get();
      vocabularyRepository.delete(oldVocabularyWord);
      Long oldWordFrequency = oldVocabularyWord.getFrequency();
      Optional<VocabularyWord> newVocabularyWordOptional = vocabularyRepository.findById(newWord);
      if (newVocabularyWordOptional.isPresent()) {
        VocabularyWord newVocabularyWord = newVocabularyWordOptional.get();
        newVocabularyWord.setFrequency(newVocabularyWord.getFrequency() + oldWordFrequency);
        vocabularyRepository.save(newVocabularyWord);
      } else {
        vocabularyRepository.save(VocabularyWord.builder()
          .word(newWord)
          .frequency(oldWordFrequency)
          .build());
      }
    }
  }

  @Override
  public Optional<VocabularyWord> findByWord(String word) {
    return vocabularyRepository.findById(word);
  }

  @Override
  public VocabularyWord getWord(String word) {
    return vocabularyRepository.findById(word).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public VocabularyWordCanonical getCanonicalWord(String word) {
    List<WordCanonical> words = wordCanonicalRepository.findByWord(word);
    if (CollectionUtils.isEmpty(words)) {
      return VocabularyWordCanonical.builder().word(word).build();
    }
    return VocabularyWordCanonical.builder()
      .word(word)
      .canonical(words.stream()
        .map(WordCanonical::getCanonical)
        .filter(canon -> !StringUtils.isEmpty(canon))
        .collect(Collectors.toSet()))
      .tags(words.stream()
        .map(WordCanonical::getTag)
        .filter(tag -> !StringUtils.isEmpty(tag))
        .collect(Collectors.toSet()))
      .frequency((long) words.size()).build();
  }
}
