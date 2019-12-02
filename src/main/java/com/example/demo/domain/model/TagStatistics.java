package com.example.demo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.Comparator;

import static com.example.demo.domain.model.Statistics.Type.TAG;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagStatistics extends Statistics {
  private static final Type type = TAG;
  private String tag;

  @Builder
  public TagStatistics(Long frequency, String tag) {
    super(frequency);
    this.tag = tag;
  }

  public static Comparator<TagStatistics> getComparator(Sort sort) {
    return (o1, o2) -> {
      Sort.Order frequency = sort.getOrderFor("frequency");
      if (frequency != null) {
        return frequency.isAscending() ? (int) (o1.getFrequency() - o2.getFrequency()) : (int) (o2.getFrequency() - o1.getFrequency());
      }
      Sort.Order tag1 = sort.getOrderFor("tag");
      if (tag1 != null) {
        return tag1.isAscending() ? o1.getTag().compareTo(o2.getTag()) : o2.getTag().compareTo(o1.getTag());
      }
      return 0;
    };
  }
}
