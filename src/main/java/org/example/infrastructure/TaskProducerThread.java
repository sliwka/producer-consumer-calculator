package org.example.infrastructure;

import org.example.tasks.Task;
import org.example.tasks.TaskProducer;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class TaskProducerThread extends Thread {

    private static final Logger logger = Logger.getLogger(TaskProducerThread.class.getName());

    private final HalfBlockingQueue<Task> taskQueue;
    private final TaskProducer taskProducer;
    private int taskCounter = 0;

    public TaskProducerThread(String name, HalfBlockingQueue<Task> taskQueue, TaskProducer taskProducer) {
        super(name);
        this.taskQueue = taskQueue;
        this.taskProducer = taskProducer;
    }

    @Override
    public void run() {
        logger.info("%s started".formatted(getName()));
        while (!isInterrupted()) {
            try {
                Task task = taskProducer.produce("Task:%s-%d".formatted(getName(), taskCounter++));
                taskQueue.offer(task);
            } catch (InterruptedException e) {
                logger.fine("%s interrupted while offering or producing".formatted(getName()));
                break;
            }
        }
        logger.info("%s stopped".formatted(getName()));
    }
}
