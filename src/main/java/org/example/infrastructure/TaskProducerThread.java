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
//                logger.fine("%s producing...".formatted(getName()));
                Task task = taskProducer.produce("Task:%s-%d".formatted(getName(), taskCounter++));
//                logger.fine("%s offering %s...".formatted(getName(), task.toString()));
                taskQueue.offer(task);
//                logger.fine("%s offered %s".formatted(getName(), task.toString()));
            } catch (InterruptedException e) {
                logger.fine("%s interrupted while offering or producing".formatted(getName()));
                break;
            }
        }
        logger.info("%s stopped".formatted(getName()));
    }
}
