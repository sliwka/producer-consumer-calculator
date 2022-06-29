package org.example;

import org.example.infrastructure.ThroughputMetrics;
import org.example.tasks.ComputationReporter;
import org.example.tasks.InterruptableRunnable;
import org.example.tasks.TaskConsumer;
import org.example.tasks.TaskProducer;
import org.example.domain.ExpressionGenerator;
import org.example.domain.Parser;
import org.example.infrastructure.ProducerConsumerRunner;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final int PRODUCER_THREADS_COUNT = 2;
    private static final int CONSUMER_THREADS_COUNT = 4;
    private static final int QUEUE_CAPACITY = 5;
    private static final int PRODUCER_DELAY_MILLISECONDS = 100;
    private static final int CONSUMER_DELAY_MILLISECONDS = 100;
    public static final int RUN_DURATION_SECONDS = 4;

    static {
        String path = Main.class
                .getClassLoader()
                .getResource("logging.properties")
                .getFile();
        System.setProperty("java.util.logging.config.file", path);
    }

    public static void main(String[] args) {
        ThroughputMetrics throughputMetrics = new ThroughputMetrics();

        ProducerConsumerRunner producerConsumerRunner = new ProducerConsumerRunner(
                PRODUCER_THREADS_COUNT, CONSUMER_THREADS_COUNT, QUEUE_CAPACITY,
                throughputMetrics,
                createTaskProducer(PRODUCER_DELAY_MILLISECONDS),
                createTaskConsumer(CONSUMER_DELAY_MILLISECONDS));

        producerConsumerRunner.run();

        try {
            Thread.sleep(Duration.ofSeconds(RUN_DURATION_SECONDS).toMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        producerConsumerRunner.stop();

        logger.info("throughput: %f tasks/s"
                .formatted((float)throughputMetrics.getConsumedTasksCount() / RUN_DURATION_SECONDS));
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