package com.example.demo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyWordCanonical {
  private String word;
  private Set<String> canonical;
  private Long frequency;
  private Set<String> tags;
}
