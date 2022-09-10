// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.*;
import java.util.regex.*;

import jthrice.launcher.*;

public final class Lexer {
  public static List<Lexeme> lex(Resolution resolution, Source source) {
    var lexer = new Lexer(resolution, source, new ArrayList<>(),
      new HashMap<>(), source.matcher(WHITESPACE), 0, null);
    for (var regular : Regular.REGULARS) {
      lexer.matchers.put(regular, regular.matcher(source));
    }
    lexer.lex();
    return lexer.lex;
  }

  private static final Pattern WHITESPACE = Pattern.compile("[ \t\n]+");

  private final Resolution            resolution;
  private final Source                source;
  private final List<Lexeme>          lex;
  private final Map<Regular, Matcher> matchers;
  private final Matcher               whitespace;

  private int     index;
  private Portion unknown;

  private Lexer(Resolution resolution, Source source, List<Lexeme> lex,
    Map<Regular, Matcher> matchers, Matcher whitespace, int index,
    Portion unknown) {
    this.resolution = resolution;
    this.source     = source;
    this.lex        = lex;
    this.matchers   = matchers;
    this.whitespace = whitespace;
    this.index      = index;
    this.unknown    = unknown;
  }

  private void lex() {
    while (source.exists(index)) {
      if (lexWhitespace() || lexToken() || lexKeyword() || lexRegular()) {
        if (unknown != null) {
          resolution.error("LEXER", unknown,
            "Could not recognize %s!".formatted(
              unknown.length() > 1 ? "these characters" : "this character"));
          lex.add(new Unknown(unknown));
          unknown = null;
        }
        continue;
      }
      if (unknown == null) {
        unknown = Portion.of(source, index, index);
      } else {
        unknown = Portion.of(source, unknown.first().index(), index);
      }
      index++;
    }
  }

  private boolean lexKeyword() {
    for (var keyword : Exact.KEYWORDS) {
      if (keyword.matches(source, index)) {
        var portion = Portion.of(source, index, index + keyword.length() - 1);
        index += keyword.length();
        lex.add(keyword.create(portion));
        if (separate()) {
          return true;
        }
        index = portion.first().index();
        lex.remove(lex.size() - 1);
      }
    }
    return false;
  }

  private boolean lexRegular() {
    for (var entry : matchers.entrySet()) {
      var regular = entry.getKey();
      var matcher = entry.getValue();
      if (matcher.find(index)) {
        if (matcher.start() != index) {
          continue;
        }
        index = matcher.end();
        lex.add(regular
          .create(Portion.of(source, matcher.start(), matcher.end() - 1)));
        if (separate()) {
          return true;
        }
        index = matcher.start();
        lex.remove(lex.size() - 1);
      }
    }
    return false;
  }

  private boolean separate() {
    return lexWhitespace() || lexToken();
  }

  private boolean lexToken() {
    for (var token : Exact.TOKENS) {
      if (token.matches(source, index)) {
        var portion = Portion.of(source, index, index + token.length() - 1);
        index += token.length();
        lex.add(token.create(portion));
        return true;
      }
    }
    return false;
  }

  private boolean lexWhitespace() {
    if (whitespace.find(index) && whitespace.start() == index) {
      index = whitespace.end();
      return true;
    }
    return false;
  }
}
