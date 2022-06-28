package org.example.domain;

public class Parser {
    public double parse(String expression) throws ParseException {
        return sumComponents(expression);
    }

    private double sumComponents(String expression) throws ParseException {
        double value = 0;
        int prevOperatorIndex = -1;
        char lasOperator = '+';
        String expressionWithTerminator = expression + "+";
        for (int i = 0; i < expressionWithTerminator.length(); i++) {
            char c = expressionWithTerminator.charAt(i);
            if (Symbols.ADDITIVE_OPERATORS.contains("" + c)) {
                if (!(i == 0 && c == '-')) {
                    double component = multiplyComponents(expressionWithTerminator.substring(prevOperatorIndex + 1, i));
                    if (lasOperator == '+') {
                        value += component;
                    } else {
                        value -= component;
                    }
                }
                prevOperatorIndex = i;
                lasOperator = c;
            }
        }
        return value;
    }

    private double multiplyComponents(String expression) throws ParseException {
        double value = 1;
        int prevOperatorIndex = -1;
        char lasOperator = '*';
        String expressionWithTerminator = expression + "*";
        for (int i = 0; i < expressionWithTerminator.length(); i++) {
            char c = expressionWithTerminator.charAt(i);
            if (Symbols.MULTIPLICATIVE_OPERATORS.contains("" + c)) {
                double component;
                try {
                    component = Double.parseDouble(expressionWithTerminator.substring(prevOperatorIndex + 1, i));
                } catch (NumberFormatException e) {
                    throw new ParseException(e);
                }
                if (lasOperator == '*') {
                    value *= component;
                } else {
                    if (component == 0.0) {
                        throw new ParseException("Division by zero");
                    }
                    value /= component;
                }
                prevOperatorIndex = i;
                lasOperator = c;
            }
        }
        return value;
    }
}
