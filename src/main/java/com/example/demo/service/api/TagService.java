package com.example.demo.service.api;

public interface TagService {
  void tagText(String text);

  void editTag(String word, String newTag);

  String annotateText(String text);

  void tagWithCanonical(String text, Long textId);
}
