package com.example.demo.service.impl;

import com.example.demo.domain.entity.Text;
import com.example.demo.domain.entity.VocabularyWord;
import com.example.demo.domain.entity.WordCanonical;
import com.example.demo.repository.TextRepository;
import com.example.demo.repository.VocabularyRepository;
import com.example.demo.repository.WordCanonicalRepository;
import com.example.demo.service.api.TagService;
import com.example.demo.service.api.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TextServiceImpl implements TextService {

  @Autowired
  private VocabularyRepository vocabularyRepository;

  @Autowired
  private TextRepository textRepository;

  @Autowired
  private TagService tagService;

  @Autowired
  private WordCanonicalRepository wordCanonicalRepository;

  @Override
  public Page<Text> getTexts(Pageable pageable) {
    return textRepository.findAll(pageable);
  }

  @Override
  public Page<Text> searchText(List<String> searchWords) {
    searchWords = searchWords.stream().filter(word -> !StringUtils.isEmpty(word)).collect(Collectors.toList());
    Set<WordCanonical> wordCanonicalList = wordCanonicalRepository.findByWordInOrCanonicalIn(searchWords, searchWords);
    Map<Long, List<WordCanonical>> textWordsMap = wordCanonicalList.stream()
      .collect(Collectors.groupingBy(WordCanonical::getTextId));
    List<Text> texts = textRepository.findAllById(textWordsMap.keySet());
    texts.sort(getSearchTextComparator(searchWords, textWordsMap));
    return new PageImpl<>(texts);
  }

  @Override
  public Text getTextById(Long id) {
    return textRepository.findById(id).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public List<VocabularyWord> getVocabularyFromText(String text, Long textId) {
    text = preprocessText(text);

    tagService.tagText(text);
    tagService.tagWithCanonical(text, textId);

    String[] words = extractWordsFromText(text);

    Map<String, Long> vocabulary = Arrays.stream(words)
      .map(String::toLowerCase)
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    Map<String, VocabularyWord> wordsToUpdate = vocabularyRepository.findByWords(vocabulary.keySet()).stream()
      .collect(Collectors.toMap(VocabularyWord::getWord, vocabularyWord -> vocabularyWord));

    List<VocabularyWord> wordsToSave = vocabulary.entrySet().stream()
      .map(wordFromText -> {
        if (wordsToUpdate.containsKey(wordFromText.getKey())) {
          VocabularyWord vocabularyWord = wordsToUpdate.get(wordFromText.getKey());
          vocabularyWord.setFrequency(vocabularyWord.getFrequency() + wordFromText.getValue());
          return vocabularyWord;
        } else {
          return VocabularyWord.builder()
            .word(wordFromText.getKey())
            .frequency(wordFromText.getValue())
            .build();
        }
      }).collect(Collectors.toList());

    return vocabularyRepository.saveAll(wordsToSave);
  }

  @Override
  public Text saveText(Long id, String fileName, String content) {
    return textRepository.save(Text.builder()
      .id(id)
      .name(fileName)
      .data(content).build());
  }

  @Override
  public Text editText(Long id, String newContent) {
    Text text = getTextById(id);
    text.setData(newContent);
    return textRepository.save(text);
  }

  @Override
  public List<Text> getTextsByIdList(List<Long> textIdList, boolean withoutContent) {
    List<Text> result = textRepository.findAllById(textIdList);
    result.forEach(text -> text.setData(null));
    return result;
  }

  @Override
  public void editWord(String oldWord, String newWord, List<Text> mentions) {
    List<Text> texts = textRepository.findAllById(mentions.stream().map(Text::getId).collect(Collectors.toList()));
    texts.forEach(text -> text.setData(text.getData().replaceAll("[.,:;?!\n\r\\s](" + oldWord + ")[.,:;?!\n\r\\s]", " " + newWord + " ")));
  }

  @Override
  public String preprocessText(String text) {
    return text
      .replaceAll("([1-9]|[12][0-9]|3[01])(st|nd|th) ", "")  // dates
      .replaceAll("[-\")(+=&^%$#@0123456789*]", " ");        // symbols
  }

  private String[] extractWordsFromText(String text) {
    return text
      .replace("'", "")
      .replaceAll("^[.,:;?!\r\n\\s]+", "")                   // trailing spaces
      .split("[.,;:!?)(\r\n\\s]+");
  }


  private Comparator<Text> getSearchTextComparator(List<String> searchWords, Map<Long, List<WordCanonical>> textWordsMap) {
    return (o1, o2) -> {

      List<WordCanonical> o1e = textWordsMap.get(o1.getId());
      List<String> o1Words = o1e.stream().map(WordCanonical::getWord).collect(Collectors.toList());
      List<String> o1Canonical = o1e.stream().map(WordCanonical::getCanonical).collect(Collectors.toList());
      final int[] o1Count = {0};
      searchWords.forEach(word -> {
        if (o1Words.contains(word) || o1Canonical.contains(word)) {
          o1Count[0]++;
        }
      });

      List<WordCanonical> o2e = textWordsMap.get(o2.getId());
      List<String> o2Words = o2e.stream().map(WordCanonical::getWord).collect(Collectors.toList());
      List<String> o2Canonical = o2e.stream().map(WordCanonical::getCanonical).collect(Collectors.toList());
      final int[] o2Count = {0};
      searchWords.forEach(word -> {
        if (o2Words.contains(word) || o2Canonical.contains(word)) {
          o2Count[0]++;
        }
      });

      return  o2Count[0] != o1Count[0]
        ? o2Count[0] - o1Count[0]
        : (o2Words.size() + o2Canonical.size() - o1Words.size() - o1Canonical.size());
    };
  }
}
