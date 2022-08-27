package jthrice.lexer;

/** Smallest meaningful group of characters in a source string. */
public class Token {
    /** Type. */
    TokenType type;
    /** Parsed value. */
    Object value;
    /** Portion in the source string. */
    Portion portion;
}
