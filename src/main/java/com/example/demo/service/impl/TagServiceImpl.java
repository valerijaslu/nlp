package com.example.demo.service.impl;

import com.example.demo.domain.entity.VocabularyWord;
import com.example.demo.domain.entity.WordCanonical;
import com.example.demo.repository.VocabularyRepository;
import com.example.demo.repository.WordCanonicalRepository;
import com.example.demo.service.api.TagService;
import com.example.demo.service.api.VocabularyService;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TagServiceImpl implements TagService {

  @Autowired
  private VocabularyService vocabularyService;

  @Autowired
  private VocabularyRepository vocabularyRepository;

  @Autowired
  private WordCanonicalRepository wordCanonicalRepository;

  private static POSModel model;

  @PostConstruct
  public void init() {
    try {
      InputStream modelStream = getClass().getClassLoader().getResourceAsStream("en-pos-maxent.bin");
      model = new POSModel(modelStream);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  public void tagText(String text) {
    String[] sentences = text.split("[.!?]+");
    Arrays.stream(sentences).forEach(this::tagSentence);
  }

  @Override
  public void editTag(String word, String newTag) {
    VocabularyWord wordToEdit = vocabularyService.getWord(word);
    wordToEdit.setTag(newTag);
    vocabularyRepository.save(wordToEdit);
  }

  @Override
  public String annotateText(String text) {
    String[] sentences = text.split("[.!?]+");
    return Arrays.stream(sentences).map(this::annotateSentence).collect(Collectors.joining(". "));
  }

  @Override
  public void tagWithCanonical(String text, Long textId) {
    try{
      // test sentence
      String[] tokens = text.split(" ");
      // Parts-Of-Speech Tagging
      // reading parts-of-speech model to a stream
      InputStream posModelIn = getClass().getClassLoader().getResourceAsStream("en-pos-maxent.bin");
      // loading the parts-of-speech model from stream
      POSModel posModel = new POSModel(posModelIn);
      // initializing the parts-of-speech tagger with model
      POSTaggerME posTagger = new POSTaggerME(posModel);
      // Tagger tagging the tokens
      String tags[] = posTagger.tag(tokens);
      // loading the dictionary to input stream
      InputStream dictLemmatizer = getClass().getClassLoader().getResourceAsStream("dictionary.txt");
      // loading the lemmatizer with dictionary
      DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(dictLemmatizer);
      // finding the lemmas
      String[] lemmas = lemmatizer.lemmatize(tokens, tags);

      List<WordCanonical> newWords = new ArrayList<>();

      StringBuilder logMessage = new StringBuilder();
      for (int i = 0; i < tokens.length; i++) {
        // replace special characters from words
        String word = tokens[i].trim().replaceAll("[.,':;?!\r\n\\s]", "").toLowerCase();

        if (!StringUtils.isEmpty(word)) {

          // replace special characters from canonical form of the word
          String lemma = lemmas[i].replaceAll("^[.,:;?!\r\n\\s]+", "");

          // use word itself as canonical form if lib can not define it
          lemma = "O".equals(lemma) ? word : lemma;

          String[] lemmaArray = new String[1];
          lemmaArray[0] = lemma;
          String canonicalTag = posTagger.tag(lemmaArray)[0].replaceAll("^[.,:;?!\r\n\\s]+", "");

          newWords.add(WordCanonical.builder()
            .word(word)
            .tag(canonicalTag)
            .canonical(lemma)
            .textId(textId)
            .build());

          logMessage.append(tokens[i]).append(" : ").append(tags[i]).append(" : ").append(lemmas[i]);
        }
      }

      log.info("word : tag : canonical form\n");
      log.info(logMessage.toString());
      wordCanonicalRepository.saveAll(newWords);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void tagSentence(String sentence) {
    try {
      if (model != null) {
        POSTaggerME tagger = new POSTaggerME(model);
        String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE.tokenize(sentence);
        String[] tags = tagger.tag(whitespaceTokenizerLine);
        List<VocabularyWord> newWords = new ArrayList<>();

        for (int i = 0; i < whitespaceTokenizerLine.length; i++) {

          // replace special characters from words
          String word = whitespaceTokenizerLine[i].trim().replaceAll("[.,':;?!\r\n\\s]", "").toLowerCase();
          if (!StringUtils.isEmpty(word)) {
            String tag = tags[i].replaceAll("^[.,:;?!\r\n\\s]+", "");

            // add new word to vocabulary, if it's not present
            if (!vocabularyService.findByWord(word).isPresent()) {
              newWords.add(VocabularyWord.builder()
                .word(word)
                .tag(tag)
                .frequency(0L)
                .build());
            }
            // else add new tag for existing word
            else {
              VocabularyWord vocabularyWord = vocabularyService.findByWord(word).get();
              vocabularyWord.setTag(updateTags(vocabularyWord.getTag(), tag));
              newWords.add(vocabularyWord);
            }

          }
        }

        vocabularyRepository.saveAll(newWords);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String annotateSentence(String sentence) {
    try {
      if (model != null) {
        POSTaggerME tagger = new POSTaggerME(model);
        String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE.tokenize(sentence);
        String[] tags = tagger.tag(whitespaceTokenizerLine);
        List<String> taggedWords = new ArrayList<>();
        for (int i = 0; i < whitespaceTokenizerLine.length; i++) {
          String word = whitespaceTokenizerLine[i].trim().replaceAll("[.,':;?!\r\n\\s]", "").toLowerCase();
          if (!StringUtils.isEmpty(word)) {
            String tag = tags[i].replaceAll("^[.,:;?!\r\n\\s]+", "");
            taggedWords.add(String.format("%s{%s}", word, tag));
          }
        }
        return String.join(" ", taggedWords);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  private String updateTags(String oldTags, String newTag) {
    if (StringUtils.isEmpty(oldTags) || StringUtils.isEmpty(newTag)) {
      return newTag;
    }
    String[] tags = oldTags.split(",");
    if (!Arrays.asList(tags).contains(newTag)) {
      oldTags += "," + newTag;
    }
    return oldTags;
  }
}
