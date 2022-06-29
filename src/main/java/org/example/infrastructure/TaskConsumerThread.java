package org.example.infrastructure;

import org.example.tasks.Task;
import org.example.tasks.TaskConsumer;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class TaskConsumerThread extends Thread {

    private static final Logger logger = Logger.getLogger(TaskConsumerThread.class.getName());

    private final HalfBlockingQueue<Task> taskQueue;
    private final TaskConsumer taskConsumer;
    private final ThroughputMetrics throughputMetrics;

    public TaskConsumerThread(String name, HalfBlockingQueue<Task> taskQueue, TaskConsumer taskConsumer,
                              ThroughputMetrics throughputMetrics) {
        super(name);
        this.taskQueue = taskQueue;
        this.taskConsumer = taskConsumer;
        this.throughputMetrics = throughputMetrics;
    }

    @Override
    public void run() {
        logger.info("%s started".formatted(getName()));
        while (!isInterrupted()) {
            try {
                Task task = taskQueue.take();
                taskConsumer.consume(task);
                throughputMetrics.incrementConsumedTasksCount();
            } catch (InterruptedException e) {
                logger.fine("%s interrupted while polling or consuming".formatted(getName()));
                break;
            }
        }
        logger.info("%s stopped".formatted(getName()));
    }

}
