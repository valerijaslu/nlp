package com.example.demo.service;

import com.example.demo.domain.entity.Text;
import com.example.demo.domain.entity.VocabularyWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TextService {

    Page<Text> getTexts(Pageable pageable);

    List<VocabularyWord> getVocabularyFromText(String text);

    Text saveText(String fileName, String content);
}
