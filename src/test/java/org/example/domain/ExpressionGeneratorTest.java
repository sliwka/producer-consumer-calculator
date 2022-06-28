package org.example.domain;

import org.example.domain.ExpressionGenerator;
import org.example.domain.ParseException;
import org.example.domain.Parser;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.fail;

class ExpressionGeneratorTest {

    @Test
    public void testExpressionsCorrectness() {
        int testSize = 100;
        Random random = new Random(0); // deterministic tests
        ExpressionGenerator expressionGenerator = new ExpressionGenerator(() -> random);
        Parser parser = new Parser();
        for (int i = 0; i < testSize; i++) {
            String expression = expressionGenerator.generateExpression();
            try {
                parser.parse(expression);
            } catch (ParseException e) {
                fail("generated incorrect expression: %s".formatted(expression));
            }
        }
    }
}