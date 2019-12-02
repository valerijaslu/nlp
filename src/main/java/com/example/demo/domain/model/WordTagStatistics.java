package com.example.demo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.Comparator;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordTagStatistics extends Statistics {
  private static final Type type = Type.WORD_TAG;
  private String tag;
  private String word;

  @Builder
  public WordTagStatistics(Long frequency, String tag, String word) {
    super(frequency);
    this.tag = tag;
    this.word = word;
  }

  public static Comparator<WordTagStatistics> getComparator(Sort sort) {
    return (o1, o2) -> {
      Sort.Order frequency = sort.getOrderFor("frequency");
      if (frequency != null) {
        return frequency.isAscending() ? (int) (o1.getFrequency() - o2.getFrequency()) : (int) (o2.getFrequency() - o1.getFrequency());
      }
      Sort.Order tag = sort.getOrderFor("tag");
      if (tag != null) {
        return tag.isAscending() ? o1.getTag().compareTo(o2.getTag()) : o2.getTag().compareTo(o1.getTag());
      }
      Sort.Order word = sort.getOrderFor("word");
      if (word != null) {
        return word.isAscending() ? o1.getWord().compareTo(o2.getWord()) : o2.getWord().compareTo(o1.getWord());
      }
      return 0;
    };
  }
}
