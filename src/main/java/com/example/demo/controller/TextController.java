package com.example.demo.controller;

import com.example.demo.domain.entity.Text;
import com.example.demo.domain.entity.VocabularyWord;
import com.example.demo.service.api.AnnotatedTextService;
import com.example.demo.service.api.TextService;
import com.example.demo.service.api.VocabularyWordTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    private AnnotatedTextService annotatedTextService;

    @Autowired
    private VocabularyWordTextService vocabularyWordTextService;

    @GetMapping
    public Page<Text> text(Pageable pageable) {
        Page<Text> texts = textService.getTexts(pageable);
        return texts;
    }

    @PostMapping
    public List<VocabularyWord> addText(@RequestParam MultipartFile file) throws IOException {
        String content = new String(file.getBytes(), UTF_8);
        Text text = textService.saveText(null, file.getOriginalFilename(), content);
        annotatedTextService.annotateText(text);
        List<VocabularyWord> vocabularyFromText = textService.getVocabularyFromText(content, text.getId());
        vocabularyWordTextService.saveWordsFromText(
          vocabularyFromText.stream()
          .map(VocabularyWord::getWord)
          .collect(Collectors.toList()), text.getId());
        return vocabularyFromText;
    }

    @PutMapping("{textId}")
    public Text editText(@RequestParam MultipartFile file, @PathVariable Long textId) throws IOException {
        String content = new String(file.getBytes(), UTF_8);
        return textService.editText(textId, content);
    }
}
