package com.example.demo.repository;

import com.example.demo.domain.entity.VocabularyWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

  private static final RowMapper<VocabularyWord> vocabularyWordRowMapper = (ResultSet rs, int rowNum) ->
    VocabularyWord.builder()
      .word(rs.getString("word"))
      .frequency(rs.getLong("frequency"))
      .tag(rs.getString("tag"))
      .build();

  @Autowired
  public VocabularyRepositoryCustomImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<VocabularyWord> findByWords(Collection<String> words) {
    String sql = "select\n" +
      "       word, frequency, tag\n" +
      "       from vocabulary\n" +
      "       where " + toOracleInWithoutLimitations(new ArrayList<>(words), "word") + "\n";
    return jdbcTemplate.query(sql, vocabularyWordRowMapper);
  }

  @Override
  public Page<VocabularyWord> findByWords(Collection<String> words, Pageable pageable) {
    Sort sort = pageable.getSort();
    String sortField = sort.getOrderFor("frequency") != null ? "frequency" : "word";
    String sortOrder = sort.getOrderFor(sortField) != null ? sort.getOrderFor(sortField).getDirection().toString() : "ASC";
    long offset = pageable.getOffset();
    long pageSize = pageable.getPageSize();
    String sql = "select\n" +
      "       word, frequency, tag\n" +
      "       from vocabulary\n" +
      "       where " + toOracleInWithoutLimitations(new ArrayList<>(words), "word") + "\n" +
      String.format("order by %s %s\n", sortField, sortOrder) +
      String.format("offset %d rows fetch next %d rows only \n", offset, pageSize);
    return new PageImpl<>(jdbcTemplate.query(sql, vocabularyWordRowMapper), pageable, countWords(words));
  }

  private long countWords(Collection<String> words) {
    String sql = "select count(*)\n" +
      "       from vocabulary\n" +
      "       where " + toOracleInWithoutLimitations(new ArrayList<>(words), "word") + "\n";
    return jdbcTemplate.queryForObject(sql, Long.class);
  }
}
