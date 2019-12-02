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
public class BiGramStatistics extends Statistics{
  private static final Type type = Type.BI_GRAM;
  private String previousTag;
  private String nextTag;

  @Builder
  public BiGramStatistics(Long frequency, String previousTag, String nextTag) {
    super(frequency);
    this.previousTag = previousTag;
    this.nextTag = nextTag;
  }

  public static Comparator<BiGramStatistics> getComparator(Sort sort) {
    return (o1, o2) -> {
      Sort.Order frequency = sort.getOrderFor("frequency");
      if (frequency != null) {
        return frequency.isAscending() ? (int) (o1.getFrequency() - o2.getFrequency()) : (int) (o2.getFrequency() - o1.getFrequency());
      }
      Sort.Order previousTag = sort.getOrderFor("previousTag");
      if (previousTag != null) {
        return previousTag.isAscending() ? o1.getPreviousTag().compareTo(o2.getPreviousTag()) : o2.getPreviousTag().compareTo(o1.getPreviousTag());
      }
      Sort.Order nextTag = sort.getOrderFor("nextTag");
      if (nextTag != null) {
        return nextTag.isAscending() ? o1.getNextTag().compareTo(o2.getNextTag()) : o2.getNextTag().compareTo(o1.getNextTag());
      }
      return 0;
    };
  }
}
