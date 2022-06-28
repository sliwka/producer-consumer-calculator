package org.example;

import org.example.tasks.ComputationReporter;
import org.example.tasks.InterruptableRunnable;
import org.example.tasks.TaskConsumer;
import org.example.tasks.TaskProducer;
import org.example.domain.ExpressionGenerator;
import org.example.domain.Parser;
import org.example.infrastructure.ProducerConsumerRunner;

import java.util.concurrent.ThreadLocalRandom;

public class Main {

    static {
        String path = Main.class
                .getClassLoader()
                .getResource("logging.properties")
                .getFile();
        System.setProperty("java.util.logging.config.file", path);
    }

    public static void main(String[] args) {
        new ProducerConsumerRunner(2, 4, 5,
                createTaskProducer(100),
                createTaskConsumer(100))
                .run(4);
    }

    private static TaskProducer createTaskProducer(int delayMilliseconds) {
        ExpressionGenerator expressionGenerator = new ExpressionGenerator(ThreadLocalRandom::current);
        InterruptableRunnable runnableAddon = () -> Thread.sleep(delayMilliseconds);
        return new TaskProducer(expressionGenerator, runnableAddon);
    }

    private static TaskConsumer createTaskConsumer(int delayMilliseconds) {
        Parser parser = new Parser();
        ComputationReporter computationReporter = new ComputationReporter() {
            @Override
            public void reportComputedValue(String expression, double computedValue) {
                System.out.printf("  %s = %f%n", expression, computedValue);
            }

            @Override
            public void reportError(String expression, String message) {
                System.err.printf("  %s = ERROR: %s", expression, message);
            }
        };
        InterruptableRunnable runnableAddon = () -> Thread.sleep(delayMilliseconds);
        return new TaskConsumer(parser, computationReporter, runnableAddon);
    }
}