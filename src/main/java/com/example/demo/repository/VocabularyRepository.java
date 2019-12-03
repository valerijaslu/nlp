package com.example.demo.repository;

import com.example.demo.domain.entity.VocabularyWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VocabularyRepository extends JpaRepository<VocabularyWord, String>, VocabularyRepositoryCustom {

  Page<VocabularyWord> findByWordLike(String word, Pageable page);

}
