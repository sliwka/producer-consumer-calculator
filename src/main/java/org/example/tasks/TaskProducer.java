package org.example.tasks;

import org.example.domain.ExpressionGenerator;

public class TaskProducer {

    private final ExpressionGenerator expressionGenerator;
    private final InterruptableRunnable runnableAddon;

    public TaskProducer(ExpressionGenerator expressionGenerator, InterruptableRunnable runnableAddon) {
        this.expressionGenerator = expressionGenerator;
        this.runnableAddon = runnableAddon;
    }

    public Task produce(String name) throws InterruptedException {
        runnableAddon.run();
        String expression = expressionGenerator.generateExpression();
        return new Task(name, expression);
    }


}
