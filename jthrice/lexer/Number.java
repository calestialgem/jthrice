// SPDX-FileCopyrightText: 2022 Cem Geçgel <gecgelcem@outlook.com>
// SPDX-License-Identifier: GPL-3.0-or-later

package jthrice.lexer;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/** Number literal. */
public sealed abstract class Number extends Token permits Number.Integer, Number.Real {
    /** Number at the given location. */
    public static Optional<Token> of(Location first) {
        final String DIGITS = "0123456789";
        final BigDecimal BASE = new BigDecimal(DIGITS.length());

        Location current = first;
        int digit = DIGITS.indexOf(current.get());
        if (digit == -1) {
            return Optional.empty();
        }

        Optional<java.lang.Integer> decimalPlaces = Optional.empty();
        BigDecimal value = new BigDecimal(0);
        while (digit != -1) {
            value = value.multiply(BASE).add(new BigDecimal(digit));
            if (decimalPlaces.isPresent()) {
                decimalPlaces = Optional.of(decimalPlaces.get() + 1);
            }
            Optional<Location> next = current.next();
            if (next.isEmpty()) {
                break;
            }
            current = next.get();
            if (current.get() == '.') {
                if (decimalPlaces.isPresent()) {
                    return Optional.empty();
                }
                decimalPlaces = Optional.of(0);
                next = current.next();
                if (next.isEmpty()) {
                    return Optional.empty();
                }
                current = next.get();
            }
            digit = DIGITS.indexOf(current.get());
        }

        Portion portion = new Portion(first, current.previous().get());

        if (decimalPlaces.isEmpty()) {
            return Optional.of(new Integer(portion, value));
        }
        if (decimalPlaces.get() == 0) {
            return Optional.empty();
        }
        BigDecimal dividor = BASE.pow(decimalPlaces.get());
        value = value.divide(dividor);
        return Optional.of(new Real(portion, value));
    }

    /** Integer literal. */
    public static final class Integer extends Number {
        public Integer(Portion portion, BigDecimal value) {
            super(portion, value);
        }
    }

    /** Real literal. */
    public static final class Real extends Number {
        public Real(Portion portion, BigDecimal value) {
            super(portion, value);
        }
    }

    /** Value of the number. */
    public final BigDecimal value;

    public Number(Portion portion, BigDecimal value) {
        super(portion);
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Number)) {
            return false;
        }
        Number other = (Number) obj;
        return Objects.equals(value, other.value);
    }
}
