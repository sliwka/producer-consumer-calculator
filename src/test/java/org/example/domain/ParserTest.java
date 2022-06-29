package org.example.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class ParserTest {

    @ParameterizedTest
    @CsvSource({
            "123+23/2-4/3*6,    126.5",
            "-3/4,              -0.75",
            "0,                 0",
            "1+2-4,             -1",
    })
    void testSimple(String expression, double expectedValue) throws ParseException {
        double value = new Parser().parse(expression);
        Assertions.assertEquals(expectedValue, value, 1e-6);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "12+a",
            "2-3/0",
            "+0",
            "7*",
            "*7",
            "7-",
            "0--3",
            "0*/3",
            "-",
            "+",
            "*",
            "/",
            "",
    })
    void testInvalidInput(String expression) {
        Assertions.assertThrowsExactly(ParseException.class, () -> new Parser().parse(expression));
    }

    @Test
    void testMultipleUsageOfParser() throws ParseException {
        Parser parser = new Parser();
        Assertions.assertEquals(2, parser.parse("1+1"));
        Assertions.assertEquals(3, parser.parse("1+2"));
    }
}