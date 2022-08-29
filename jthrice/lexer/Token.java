// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jthrice.Bug;
import jthrice.launcher.Source;

/** Smallest meaningful group of characters in a source. */
public class Token {
    /** Type of a token. */
    public static enum Type {
        PLUS, MINUS, STAR, FORWARD_SLASH, PERCENT, EQUAL, SEMICOLON, EOF,
        NUMBER, LET, IDENTIFIER;

        /** Token type of the given character if its a mark. */
        public static Optional<Type> ofMark(char character) {
            final String MARKS = "+-*/%=;" + Source.EOF;
            int index = MARKS.indexOf(character);
            if (index == -1) {
                return Optional.empty();
            }
            Bug.check(index < values().length, "The marks in the string are more than the token types!");
            return Optional.of(values()[index]);
        }

        /** Token type of the given string if its a keyword. */
        public static Optional<Type> ofKeyword(String keyword) {
            final String[] KEYWORDS = { "let" };
            int index = List.of(KEYWORDS).indexOf(keyword);
            if (index == -1) {
                return Optional.empty();
            }
            index += LET.ordinal();
            Bug.check(index < values().length, "The keywords in the array are more than the token types!");
            return Optional.of(values()[index]);
        }
    }

    /** Type. */
    public final Type type;
    /** Parsed value. */
    public final Object value;
    /** Portion in the source. */
    public final Portion portion;

    public Token(Type type, Object value, Portion portion) {
        this.type = type;
        this.value = value;
        this.portion = portion;
    }

    /** Whether the type of the token is one of the given types. */
    public boolean check(Type... types) {
        for (Type type : types) {
            if (this.type.equals(type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Token [" + (portion != null ? "portion=" + portion + ", " : "")
                + (type != null ? "type=" + type + ", " : "") + (value != null ? "value=" + value : "") + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(portion, type, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Token)) {
            return false;
        }
        Token other = (Token) obj;
        return Objects.equals(portion, other.portion) && type == other.type && Objects.equals(value, other.value);
    }
}
