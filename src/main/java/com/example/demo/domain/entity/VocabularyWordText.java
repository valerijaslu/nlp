package com.example.demo.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "vocabulary_text")
public class VocabularyWordText {
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;
  private Long textId;
  private String word;
}
