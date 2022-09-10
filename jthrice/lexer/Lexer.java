// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.*;
import java.util.regex.*;

import jthrice.launcher.*;
import jthrice.lexer.Token.*;

public final class Lexer {
  public static List<Token> lex(Resolution resolution, Source source) {
    var lexer = new Lexer(resolution, source, new ArrayList<>(),
      new HashMap<>(), source.matcher(WHITESPACE), 0, null);
    for (var regular : REGULARS) {
      lexer.matchers.put(regular, source.matcher(regular.pattern));
    }
    lexer.lex();
    return lexer.lex;
  }

  private static final Pattern             WHITESPACE = Pattern
    .compile("[ \t\n]+");
  private static final List<Token.Regular> REGULARS   = List.of(Token.DECIMAL,
    Token.IDENTIFIER);
  private static final List<Token.Exact>   RESERVEDS  = List.of(Token.I1,
    Token.I2, Token.I4, Token.I8, Token.IX, Token.U1, Token.U2, Token.U4,
    Token.U8, Token.UX, Token.F4, Token.F8);
  private static final List<Token.Exact>   SEPARATORS;

  static {
    // Sort from the longest to shortes; thus, when iterated separators that
    // start the same check the longer version first.
    // Ex: `>` vs `>=`, the second one should be checked first.
    var separators = new ArrayList<>(
      List.of(Token.PLUS, Token.MINUS, Token.STAR, Token.FORWARD_SLASH,
        Token.PERCENT, Token.EQUAL, Token.COLON, Token.SEMICOLON,
        Token.OPENING_PARENTHESES, Token.CLOSING_PARENTHESES, Token.EOF));
    // Achived by comparing the strings after inverting. Because longer string
    // come after the shorter ones.
    separators.sort((left, right) -> right.lexeme.compareTo(left.lexeme));
    SEPARATORS = separators;
  }

  private final Resolution                  resolution;
  private final Source                      source;
  private final List<Token>                 lex;
  private final Map<Token.Regular, Matcher> matchers;
  private final Matcher                     whitespace;

  private int     index;
  private Portion unknown;

  private Lexer(Resolution resolution, Source source, List<Token> lex,
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
      if (lexWhitespace() || lexSeparator() || lexReserved() || lexRegular()) {
        if (unknown != null) {
          resolution.error("LEXER", unknown, "Could not recognize %s!"
            .formatted(
              unknown.length() > 1 ? "these characters" : "this character"));
          lex.add(Token.of(unknown));
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

  private boolean lexReserved() {
    for (var reserved : RESERVEDS) {
      if (source.matches(reserved.lexeme, index)) {
        var portion = Portion.of(source, index,
          index + reserved.lexeme.length() - 1);
        index += reserved.lexeme.length();
        if (!separate()) {
          index = portion.first().index();
          continue;
        }
        lex.add(Token.of(reserved, portion));
        return true;
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
        if (!separate()) {
          index = matcher.start();
          continue;
        }
        lex.add(Token.of(regular,
          Portion.of(source, matcher.start(), matcher.end() - 1)));
        return true;
      }
    }
    return false;
  }

  private boolean separate() {
    return lexWhitespace() || lexSeparator();
  }

  private boolean lexSeparator() {
    for (var separator : SEPARATORS) {
      if (source.matches(separator.lexeme, index)) {
        var portion = Portion.of(source, index,
          index + separator.lexeme.length() - 1);
        index += separator.lexeme.length();
        lex.add(Token.of(separator, portion));
        return true;
      }
    }
    return false;
  }

  private boolean lexWhitespace() {
    if (whitespace.find(index)) {
      if (whitespace.start() == index) {
        index = whitespace.end();
        return true;
      }
    }
    return false;
  }
}
