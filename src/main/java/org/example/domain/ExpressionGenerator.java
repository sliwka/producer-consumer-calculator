package org.example.domain;

import java.util.Random;
import java.util.function.Supplier;

import static org.example.domain.Symbols.*;

public class ExpressionGenerator {

    private final Supplier<Random> randomSupplier;

    public ExpressionGenerator(Supplier<Random> randomSupplier) {
        this.randomSupplier = randomSupplier;
    }

    public String generateExpression() {
        int length = randomSupplier.get().nextInt(30) + 1;
        StringBuilder stringBuilder = new StringBuilder(length);
        char prevChar = '+';
        int endingDigitsCount = 0;
        for (int i = 0; i < length; i++) {
            char c = randomChar(prevChar, endingDigitsCount);
            stringBuilder.append(c);
            if (isDigit(c)) {
                endingDigitsCount++;
            } else {
                endingDigitsCount = 0;
            }
            prevChar = c;
        }
        String expression = stringBuilder.toString();
        if (isOperator(expression.charAt(expression.length() - 1))) {
            expression = expression.substring(0, expression.length() - 1);
        }
        return expression;
    }

    private boolean isDigit(char c) {
        return DIGITS.contains("" + c);
    }

    private boolean isOperator(char c) {
        return OPERATORS.contains("" + c);
    }

    private char randomChar(char prevChar, int endingDigitsCount) {
        String charSetToRandom;
        if (isOperator(prevChar)) {
            charSetToRandom = DIGITS_WITHOUT_ZERO;
        } else if (endingDigitsCount >= 9) {
            charSetToRandom = OPERATORS;
        } else {
            charSetToRandom = SYMBOLS;
        }
        return charSetToRandom.charAt(randomSupplier.get().nextInt(charSetToRandom.length()));
    }
}
