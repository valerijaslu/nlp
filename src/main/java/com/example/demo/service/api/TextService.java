package com.example.demo.service.api;

import com.example.demo.domain.entity.Text;
import com.example.demo.domain.entity.VocabularyWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TextService {

    Page<Text> getTexts(Pageable pageable);

    Page<Text> searchText(List<String> searchWords);

    Text getTextById(Long id);

    Text saveText(Long id, String fileName, String content);

    Text editText(Long id, String newContent);

    List<Text> getTextsByIdList(List<Long> textIdList, boolean withoutContent);

    List<VocabularyWord> getVocabularyFromText(String text, Long textId);

    void editWord(String oldWord, String newWord, List<Text> mentions);

    String preprocessText(String text);
}
