// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.*;
import java.util.regex.*;

import jthrice.launcher.*;

public final class Lexer {
  public static List<Token> lex(Resolution resolution, Source source) {
    var lexer = new Lexer(resolution, source);
    lexer.lex();
    return lexer.lex;
  }

  private static final Pattern             WHITESPACE = Pattern
    .compile("^[ \t\n]+");
  private static final List<Token.Regular> REGULARS   = List.of(Token.DECIMAL,
    Token.IDENTIFIER);
  private static final List<Token.Exact>   EXACTS;

  static {
    // Sort from the longest to shortes; thus, when iterated tokens that start
    // the same check the longer version first.
    // Ex: `>` vs `>=`, the second one should be checked first.
    var exacts = new ArrayList<>(List.of(Token.I1, Token.I2, Token.I4, Token.I8,
      Token.IX, Token.U1, Token.U2, Token.U4, Token.U8, Token.UX, Token.F4,
      Token.F8, Token.PLUS, Token.MINUS, Token.STAR, Token.FORWARD_SLASH,
      Token.PERCENT, Token.EQUAL, Token.COLON, Token.SEMICOLON,
      Token.OPENING_PARENTHESES, Token.CLOSING_PARENTHESES, Token.EOF));
    // Achived by comparing the strings after inverting. Because longer string
    // come after the shorter ones.
    exacts.sort((left, right) -> right.lexeme.compareTo(left.lexeme));
    EXACTS = exacts;
  }

  private final Resolution  resolution;
  private final Source      source;
  private final List<Token> lex;

  private int     index;
  private Portion unknown;

  private Lexer(Resolution resolution, Source source) {
    this.resolution = resolution;
    this.source     = source;
    lex             = new ArrayList<>();
    index           = 0;
    unknown         = null;
  }

  private void lex() {
    loop: while (source.exists(index)) {
      var whitespace = WHITESPACE.matcher(source.contents);
      if (whitespace.find()) {
        index = whitespace.end();
        unknown();
        continue;
      }
      for (var exact : EXACTS) {
        if (source.contents.startsWith(exact.lexeme, index)) {
          var portion = Portion.of(source, index,
            index + exact.lexeme.length() - 1);
          index += exact.lexeme.length();
          if (exact.separate && !separate()) {
            index = portion.first.index;
            continue;
          }
          lex.add(Token.of(exact, portion));
          unknown();
          continue loop;
        }
      }
      for (var regular : REGULARS) {
        var matcher = regular.pattern.matcher(source.contents);
        if (matcher.find(index)) {
          index = matcher.end();
          if (regular.separate && !separate()) {
            index = matcher.start();
            continue;
          }
          lex.add(Token.of(regular,
            Portion.of(source, matcher.start(), matcher.end() - 1)));
          unknown();
          continue loop;
        }
      }
      if (unknown == null) {
        unknown = Portion.of(source, index, index);
      } else {
        unknown = Portion.of(source, unknown.first.index, index);
      }
    }
    unknown();
  }

  private boolean separate() {
    var matcher = WHITESPACE.matcher(source.contents);
    if (matcher.find(index)) {
      index = matcher.end();
      return true;
    }
    return false;
  }

  private void unknown() {
    if (unknown == null) {
      return;
    }
    resolution.error("LEXER", unknown, "Could not recognize these character%s!"
      .formatted(unknown.length() > 1 ? "s" : ""));
    lex.add(Token.of(unknown));
    unknown = null;
  }
}
