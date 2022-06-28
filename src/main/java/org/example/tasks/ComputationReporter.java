package org.example.tasks;

public interface ComputationReporter {

    void reportComputedValue(String expression, double computedValue);

    void reportError(String expression, String message);
}
