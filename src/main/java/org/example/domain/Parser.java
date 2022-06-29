package org.example.domain;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    public double parse(String expression) throws ParseException {
        return sumComponents(expression);
    }

    private double sumComponents(String expression) throws ParseException {
        double value = 0;
        List<ComponentWithOperator> componentsWithOperator =
                splitIntoComponents(expression, Symbols.ADDITIVE_OPERATORS, '+');
        handlePossibleMinusAtTheStart(componentsWithOperator);
        for (ComponentWithOperator componentWithOperator : componentsWithOperator) {
            double component = multiplyComponents(componentWithOperator.getComponent());
            if (componentWithOperator.getOperator() == '+') {
                value += component;
            } else {
                value -= component;
            }
        }
        return value;
    }

    private double multiplyComponents(String expression) throws ParseException {
        double value = 1;
        List<ComponentWithOperator> componentsWithOperator =
                splitIntoComponents(expression, Symbols.MULTIPLICATIVE_OPERATORS, '*');
        for (ComponentWithOperator componentWithOperator : componentsWithOperator) {
            double component;
            try {
                component = Double.parseDouble(componentWithOperator.getComponent());
            } catch (NumberFormatException e) {
                throw new ParseException(e);
            }
            if (componentWithOperator.getOperator() == '*') {
                value *= component;
            } else {
                if (component == 0.0) {
                    throw new ParseException("Division by zero");
                }
                value /= component;
            }
        }
        return value;
    }

    private List<ComponentWithOperator> splitIntoComponents(String expression, String operators, char defaultOperator) {
        List<ComponentWithOperator> componentsWithOperator = new ArrayList<>();
        int prevOperatorIndex = -1;
        char lasOperator = defaultOperator;
        char anyOperator = operators.charAt(0);
        String expressionWithTerminator = expression + anyOperator;
        for (int i = 0; i < expressionWithTerminator.length(); i++) {
            char character = expressionWithTerminator.charAt(i);
            if (operators.contains("" + character)) {
                componentsWithOperator.add(
                        new ComponentWithOperator(lasOperator, expressionWithTerminator.substring(prevOperatorIndex + 1, i)));
                prevOperatorIndex = i;
                lasOperator = character;
            }
        }
        return componentsWithOperator;
    }

    private void handlePossibleMinusAtTheStart(List<ComponentWithOperator> componentsWithOperator) {
        if (componentsWithOperator.size() >= 2) {
            ComponentWithOperator component0 = componentsWithOperator.get(0);
            ComponentWithOperator component1 = componentsWithOperator.get(1);
            if (component0.getComponent().isEmpty() && component1.getOperator() == '-') {
                componentsWithOperator.set(0, new ComponentWithOperator(component0.getOperator(), "0"));
            }
        }
    }
}
