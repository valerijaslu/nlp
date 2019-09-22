package com.example.demo.controller;

import com.example.demo.domain.entity.Text;
import com.example.demo.domain.entity.VocabularyWord;
import com.example.demo.service.TextService;
import com.example.demo.service.VocabularyWordTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@RestController
@RequestMapping("text")
public class TextController {

    @Autowired
    private TextService textService;

    @Autowired
    private VocabularyWordTextService vocabularyWordTextService;

    @PostMapping
    public List<VocabularyWord> addText(@RequestParam MultipartFile file) throws IOException {
        String content = new String(file.getBytes(), UTF_8);
        Text text = textService.saveText(file.getOriginalFilename());
        List<VocabularyWord> vocabularyFromText = textService.getVocabularyFromText(content);
        vocabularyWordTextService.saveWordsFromText(
          vocabularyFromText.stream()
          .map(VocabularyWord::getWord)
          .collect(Collectors.toList()), text.getId());
        return vocabularyFromText;
    }
}
