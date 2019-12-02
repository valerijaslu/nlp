package com.example.demo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Statistics {

  public static enum Type {
    TAG, BI_GRAM, WORD_TAG
  }

  private Long frequency;
}
