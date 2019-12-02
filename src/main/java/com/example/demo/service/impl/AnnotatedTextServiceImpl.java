package com.example.demo.service.impl;

import com.example.demo.domain.entity.AnnotatedText;
import com.example.demo.domain.entity.Text;
import com.example.demo.domain.model.BiGramStatistics;
import com.example.demo.domain.model.Statistics;
import com.example.demo.domain.model.TagStatistics;
import com.example.demo.domain.model.WordTagStatistics;
import com.example.demo.repository.AnnotatedTextRepository;
import com.example.demo.service.api.AnnotatedTextService;
import com.example.demo.service.api.TagService;
import com.example.demo.service.api.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AnnotatedTextServiceImpl implements AnnotatedTextService {

  @Autowired
  private TextService textService;

  @Autowired
  private TagService tagService;

  @Autowired
  private AnnotatedTextRepository annotatedTextRepository;

  @Override
  public AnnotatedText annotateText(Text text) {
    String preprocessedText = textService.preprocessText(text.getData());
    String annotatedText = tagService.annotateText(preprocessedText);
    return annotatedTextRepository.save(AnnotatedText.builder()
      .id(text.getId()).name(text.getName()).data(annotatedText).build());
  }

  @Override
  public AnnotatedText editAnnotatedText(String editedText, Long id) {
    AnnotatedText annotatedText = annotatedTextRepository.findById(id).orElseThrow(NoSuchElementException::new);
    annotatedText.setData(editedText);
    return annotatedTextRepository.save(annotatedText);
  }

  @Override
  public Page<AnnotatedText> getAnnotatedTexts(Pageable pageable) {
    return annotatedTextRepository.findAll(pageable);
  }

  @Override
  public List<Statistics> getStatistics(Statistics.Type statisticsType, Sort sort) {
    List<AnnotatedText> annotatedTextList = annotatedTextRepository.findAll();
    String data = annotatedTextList.stream()
      .map(AnnotatedText::getData)
      .collect(Collectors.joining(". "));
    return getStatisticsByType(data, statisticsType, sort);
  }

  private List<Statistics> getStatisticsByType(String data, Statistics.Type type, Sort sort) {
    List<Statistics> statistics = new ArrayList<>();
    switch (type) {
      case TAG: {
        statistics = tagsStatistics(data, sort);
        break;
      }
      case BI_GRAM: {
        statistics = biGramsStatistics(data, sort);
        break;
      }
      case WORD_TAG: {
        statistics = wordsStatistics(data, sort);
        break;
      }
      default: {
        break;
      }
    }
    return statistics;
  }

  private List<Statistics> tagsStatistics(String text, Sort sort) {
    Comparator<TagStatistics> comparator = TagStatistics.getComparator(sort);
    List<String> list = getOrderedTagList(text);
    return getGroupedStatisticsData(list).entrySet().stream()
      .map(entry -> TagStatistics.builder()
        .frequency(entry.getValue())
        .tag(entry.getKey())
        .build())
      .sorted(comparator)
      .collect(Collectors.toList());
  }

  private List<Statistics> biGramsStatistics(String text, Sort sort) {
    List<String> list = getOrderedTagList(text);
    List<String> biGramsList = new ArrayList<>();
    for(int i = 0; i < list.size(); i++) {
      if (i < list.size() - 1) {
        biGramsList.add(
          list.get(i) + "+" + list.get(i + 1));
      }
    }
    Comparator<BiGramStatistics> comparator = BiGramStatistics.getComparator(sort);
    return getGroupedStatisticsData(biGramsList).entrySet().stream()
      .map(entry -> BiGramStatistics.builder()
        .frequency(entry.getValue())
        .previousTag(entry.getKey().substring(0, entry.getKey().indexOf("+")))
        .nextTag(entry.getKey().substring(entry.getKey().indexOf("+") + 1))
        .build())
      .sorted(comparator)
      .collect(Collectors.toList());
  }

  private List<Statistics> wordsStatistics(String text, Sort sort) {
    Pattern pattern = Pattern.compile("\\w+\\{(\\w+)}( |.)");
    List<String> list = new ArrayList<>();
    Matcher m = pattern.matcher(text);
    while (m.find()) {
      list.add(m.group().replace(" ", "").replace(".", ""));
    }
    Comparator<WordTagStatistics> comparator = WordTagStatistics.getComparator(sort);
    return getGroupedStatisticsData(list).entrySet().stream()
      .map(entry -> WordTagStatistics.builder()
        .frequency(entry.getValue())
        .tag(entry.getKey().substring(entry.getKey().indexOf("{") + 1, entry.getKey().indexOf("}")))
        .word(entry.getKey().substring(0, entry.getKey().indexOf("{")))
        .build())
      .sorted(comparator)
      .collect(Collectors.toList());
  }

  private List<String> getOrderedTagList(String text) {
    Pattern pattern = Pattern.compile("\\w+\\{(\\w+)}( |.)");
    List<String> list = new ArrayList<>();
    Matcher m = pattern.matcher(text);
    while (m.find()) {
      list.add(m.group(1));
    }
    return list;
  }

  private Map<String, Long> getGroupedStatisticsData(List<String> list) {
    return list.stream().collect(Collectors.groupingBy(p -> p, Collectors.counting()));
  }
}
