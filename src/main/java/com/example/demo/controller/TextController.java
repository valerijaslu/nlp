package com.example.demo.controller;

import com.example.demo.service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import static java.nio.charset.StandardCharsets.UTF_8;

@RestController
@RequestMapping("text")
public class TextController {

    public static Set<String> VOCABULARY = new TreeSet<>();

    @Autowired
    private TextService textService;

    @PostMapping
    public Set<String> addTest(@RequestParam MultipartFile file) throws IOException {
        String content = new String(file.getBytes(), UTF_8);
        VOCABULARY = textService.getVocabularyFromText(content);
        return VOCABULARY;
    }
}
