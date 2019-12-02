package com.example.demo.repository;

import com.example.demo.domain.entity.WordCanonical;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordCanonicalRepository extends JpaRepository<WordCanonical, Long> {
  List<WordCanonical> findByWord(String word);

  List<WordCanonical> findByCanonical(String canonical);
}
