package com.example.demo.controller;

import com.example.demo.domain.entity.Text;
import com.example.demo.domain.entity.VocabularyWord;
import com.example.demo.service.TextService;
import com.example.demo.service.VocabularyService;
import com.example.demo.service.VocabularyWordTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("vocabulary")
public class VocabularyController {

  @Autowired
  private VocabularyService vocabularyService;

  @Autowired
  private VocabularyWordTextService vocabularyWordTextService;

  @Autowired
  private TextService textService;

  @GetMapping
  public Page<VocabularyWord> vocabulary(Pageable page) {
    Page<VocabularyWord> vocabularyWords = vocabularyService.getVocabularyWords(page);
    setMentionsInTextForWord(vocabularyWords);
    return vocabularyWords;
  }

  @GetMapping("{search}")
  public Page<VocabularyWord> searchWords(@PathVariable String search, Pageable page) {
    Page<VocabularyWord> vocabularyWords = vocabularyService.findWordsBySearchFragment(search, page);
    setMentionsInTextForWord(vocabularyWords);
    return vocabularyWords;
  }

  private void setMentionsInTextForWord(Iterable<VocabularyWord> words) {
    words.forEach(vocabularyWord -> {
      List<Long> textIdList = vocabularyWordTextService.findTextIdsByWord(vocabularyWord.getWord());
      List<Text> textNamesList = textService.getTextsByIdList(textIdList, true);
      vocabularyWord.setMentions(textNamesList);
    });
  }
}
