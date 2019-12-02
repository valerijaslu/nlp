package com.example.demo.service.api;

import com.example.demo.domain.entity.AnnotatedText;
import com.example.demo.domain.entity.Text;
import com.example.demo.domain.model.Statistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AnnotatedTextService {

  AnnotatedText annotateText(Text text);

  AnnotatedText editAnnotatedText(String editedText, Long id);

  Page<AnnotatedText> getAnnotatedTexts(Pageable pageable);

  List<Statistics> getStatistics(Statistics.Type statisticsType, Sort sort);

}
