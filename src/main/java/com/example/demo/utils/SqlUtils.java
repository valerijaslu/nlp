package com.example.demo.utils;

import com.github.jknack.handlebars.internal.text.translate.CharSequenceTranslator;
import com.github.jknack.handlebars.internal.text.translate.LookupTranslator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SqlUtils {

    private static final int MAX_ORACLE_IN_SIZE = 1000;

    public static final CharSequenceTranslator ESCAPE_SQL_TRANSLATOR = new LookupTranslator(
      new HashMap<CharSequence, CharSequence>() {{ put("'", "''"); }});

    public static String toOracleInWithoutLimitations(List<?> list, String field) {
        StringBuilder oracleIn = new StringBuilder().append("(");
        for (int i = 0; i < list.size(); i += MAX_ORACLE_IN_SIZE) {
            if (i > 0) {
                oracleIn.append("  OR\n");
            }
            int toIndex = list.size() - i > MAX_ORACLE_IN_SIZE
                    ? i + MAX_ORACLE_IN_SIZE
                    : list.size();
            oracleIn.append(field).append(" IN\n").append(toOracleIn(list.subList(i, toIndex))).append("\n");
        }
        return oracleIn.append(")").toString();
    }

    private static String toOracleIn(List<?> list) {
        return "(" + list.stream()
                .map(i -> quote(i.toString()))
                .collect(Collectors.joining(", "))
                + ")";
    }

    public static String quote(String str) {
        return "'" + escape(str) + "'";
    }

    private static String escape(String param) {
        return ESCAPE_SQL_TRANSLATOR.translate(param);
    }

}
