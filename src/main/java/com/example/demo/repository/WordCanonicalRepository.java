package com.example.demo.repository;

import com.example.demo.domain.entity.WordCanonical;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface WordCanonicalRepository extends JpaRepository<WordCanonical, Long> {
  List<WordCanonical> findByWord(String word);

  List<WordCanonical> findByCanonical(String canonical);

  Set<WordCanonical> findByWordInOrCanonicalIn(List<String> words, List<String> canonicalWords);
}
