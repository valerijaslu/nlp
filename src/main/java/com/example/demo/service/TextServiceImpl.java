package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class TextServiceImpl implements TextService {
    @Override
    public Set<String> getVocabularyFromText(String text) {
        String[] words = text
                .replaceAll("([1-9]|[12][0-9]|3[01])st|nd|th ", "") // dates
                .replaceAll("[-\"\')(+=&^:;%$#@0123456789*]", " ")  // symbols
                .replaceAll("'ve ", " have ")                       // I've
                .replaceAll(" 'm ", " am ")                         // I'm
                .replaceAll( " 's ", " has is ")                    // It's
                .replaceAll("^[.,:;?!\r\n\\s]+", "")                // trailing spaces
                .split("[.,;:!?)(\r\n\\s]+");
        return Arrays.stream(words).map(String::toLowerCase).collect(Collectors.toCollection(TreeSet::new));
    }
}
