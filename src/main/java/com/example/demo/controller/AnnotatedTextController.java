package com.example.demo.controller;

import com.example.demo.domain.entity.AnnotatedText;
import com.example.demo.domain.entity.Text;
import com.example.demo.domain.model.Statistics;
import com.example.demo.service.api.AnnotatedTextService;
import com.example.demo.service.api.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import static java.nio.charset.StandardCharsets.UTF_8;

@RestController
@RequestMapping("text")
public class AnnotatedTextController {

  @Autowired
  private TextService textService;

  @Autowired
  private AnnotatedTextService annotatedTextService;

  @GetMapping("annotate")
  public Page<AnnotatedText> text(Pageable pageable) {
    Page<AnnotatedText> texts = annotatedTextService.getAnnotatedTexts(pageable);
    return texts;
  }

  @PostMapping("annotate/{textId}")
  public AnnotatedText annotate(@PathVariable Long textId) {
    Text text = textService.getTextById(textId);
    return annotatedTextService.annotateText(text);
  }

  @PutMapping("annotate/{textId}")
  public AnnotatedText editAnnotatedText(@RequestParam MultipartFile file, @PathVariable Long textId) throws IOException {
    String newAnnotatedText = new String(file.getBytes(), UTF_8);
    return annotatedTextService.editAnnotatedText(newAnnotatedText, textId);
  }

  @GetMapping("statistics/{type}")
  public List<Statistics> getStatisticsFromAnnotatedText(@PathVariable Statistics.Type type, Sort sort) {
    return annotatedTextService.getStatistics(type, sort);
  }
}
