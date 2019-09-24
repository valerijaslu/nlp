package com.example.demo.repository;

import com.example.demo.domain.entity.VocabularyWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.example.demo.utils.SqlUtils.toOracleInWithoutLimitations;

@Service
public class VocabularyRepositoryCustomImpl implements VocabularyRepositoryCustom {

  private JdbcTemplate jdbcTemplate;

  @Autowired
  public VocabularyRepositoryCustomImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<VocabularyWord> findByWords(Collection<String> words) {
    String sql = "select\n" +
      "       word, frequency\n" +
      "       from vocabulary\n" +
      "       where " + toOracleInWithoutLimitations(new ArrayList<>(words), "word") + "\n";
    RowMapper<VocabularyWord> vocabularyWordRowMapper = (ResultSet rs, int rowNum) ->
      VocabularyWord.builder()
        .word(rs.getString("word"))
        .frequency(rs.getLong("frequency"))
        .build();
    return jdbcTemplate.query(sql, vocabularyWordRowMapper);
  }
}
