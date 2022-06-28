package org.example.domain;

public class Symbols {
    public static final String ADDITIVE_OPERATORS = "+-";
    public static final String MULTIPLICATIVE_OPERATORS = "*/";
    public static final String OPERATORS = ADDITIVE_OPERATORS + MULTIPLICATIVE_OPERATORS;
    public static final String DIGITS_WITHOUT_ZERO = "123456789";
    public static final String DIGITS = "0" + DIGITS_WITHOUT_ZERO;
    public static final String SYMBOLS = OPERATORS + DIGITS;
}
