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
        int componentsCount = randomSupplier.get().nextInt(10) + 1;
        StringBuilder stringBuilder = new StringBuilder();
        if (randomSupplier.get().nextFloat() < 0.3) {
            stringBuilder.append('-');
        }
        for (int i = 0; i < componentsCount; i++) {
            stringBuilder.append(randomNumber());
            if (i < componentsCount - 1) {
                stringBuilder.append(randomOperator());
            }
        }
        return stringBuilder.toString();
    }

    private int randomNumber() {
        return randomSupplier.get().nextInt(10000);
    }

    private char randomOperator() {
        return OPERATORS.charAt(randomSupplier.get().nextInt(OPERATORS.length()));
    }
}
