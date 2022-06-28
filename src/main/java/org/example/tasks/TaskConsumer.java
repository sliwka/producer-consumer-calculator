package org.example.tasks;


import org.example.domain.ParseException;
import org.example.domain.Parser;

import java.util.logging.Logger;

public class TaskConsumer {

    private static final Logger logger = Logger.getLogger(TaskConsumer.class.getName());

    private final Parser parser;
    private final ComputationReporter computationReporter;
    private final InterruptableRunnable runnableAddon;

    public TaskConsumer(Parser parser, ComputationReporter computationReporter, InterruptableRunnable runnableAddon) {
        this.parser = parser;
        this.computationReporter = computationReporter;
        this.runnableAddon = runnableAddon;
    }

    public void consume(Task task) throws InterruptedException {
        runnableAddon.run();
        try {
            double value = parser.parse(task.getExpression());
            computationReporter.reportComputedValue(task.getExpression(), value);
        } catch (ParseException e) {
            logger.severe(e.getMessage());
            computationReporter.reportError(task.getExpression(), e.getMessage());
        }
    }
}
