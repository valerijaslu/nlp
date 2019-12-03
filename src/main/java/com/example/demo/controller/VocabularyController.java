package com.example.demo.controller;

import com.example.demo.domain.entity.Text;
import com.example.demo.domain.entity.VocabularyWord;
import com.example.demo.domain.model.VocabularyWordCanonical;
import com.example.demo.repository.VocabularyRepository;
import com.example.demo.service.api.TextService;
import com.example.demo.service.api.VocabularyService;
import com.example.demo.service.api.VocabularyWordTextService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("vocabulary")
public class VocabularyController {

  @Autowired
  private VocabularyService vocabularyService;

  @Autowired
  private VocabularyRepository vocabularyRepository;

  @Autowired
  private VocabularyWordTextService vocabularyWordTextService;

  @Autowired
  private TextService textService;

  @Data
  private static class EditWordRequest {
    private String oldWord;
    private String newWord;
    private List<Text> mentions;
  }

  @GetMapping
  public Page<VocabularyWord> vocabulary(Pageable page) {
    Page<VocabularyWord> vocabularyWords = vocabularyService.getVocabularyWords(page);
    setMentionsInTextForWord(vocabularyWords);
    return vocabularyWords;
  }

  @GetMapping("find/{textId}")
  public Page<VocabularyWord> getVocabularyFromText(@PathVariable Long textId, Pageable page) {
    Text text = textService.getTextById(textId);
    List<String> words = vocabularyWordTextService.getWordsFromText(textId);
    Page<VocabularyWord> vocabularyWords = vocabularyRepository.findByWords(words, page);
    vocabularyWords.forEach(vocabularyWord -> vocabularyWord.setMentions(Collections.singletonList(text)));
    return vocabularyWords;
  }

  @GetMapping("{search}")
  public Page<VocabularyWord> searchWords(@PathVariable String search, Pageable page) {
    Page<VocabularyWord> vocabularyWords = vocabularyService.findWordsBySearchFragment(search, page);
    setMentionsInTextForWord(vocabularyWords);
    return vocabularyWords;
  }

  @PutMapping
  public void editWord(@RequestBody EditWordRequest request) {
    textService.editWord(request.getOldWord(), request.getNewWord(), request.getMentions());
    vocabularyWordTextService.editWord(request.getOldWord(), request.getNewWord(), request.getMentions());
    vocabularyService.editWord(request.getOldWord(), request.getNewWord());
  }

  @GetMapping("canonical/{word}")
  public VocabularyWordCanonical getCanonicalWord(@PathVariable String word) {
    return vocabularyService.getCanonicalWord(word);
  }

  private void setMentionsInTextForWord(Iterable<VocabularyWord> words) {
    words.forEach(vocabularyWord -> {
      List<Long> textIdList = vocabularyWordTextService.findTextIdsByWord(vocabularyWord.getWord());
      List<Text> textNamesList = textService.getTextsByIdList(textIdList, true);
      vocabularyWord.setMentions(textNamesList);
    });
  }
}
