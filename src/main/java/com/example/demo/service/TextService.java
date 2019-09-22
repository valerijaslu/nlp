package com.example.demo.service;

import com.example.demo.domain.entity.Text;
import com.example.demo.domain.entity.VocabularyWord;

import java.util.List;

public interface TextService {

    List<VocabularyWord> getVocabularyFromText(String text);

    Text saveText(String fileName);
}
