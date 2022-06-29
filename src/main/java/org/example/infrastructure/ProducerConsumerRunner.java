package org.example.infrastructure;

import org.example.tasks.Task;
import org.example.tasks.TaskConsumer;
import org.example.tasks.TaskProducer;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProducerConsumerRunner {

    private static final Logger logger = Logger.getLogger(TaskConsumerThread.class.getName());

    private final HalfBlockingQueue<Task> taskQueue;
    private final ThroughputMetrics throughputMetrics;
    private final List<TaskProducerThread> producerThreads;
    private final List<TaskConsumerThread> consumerThreads;

    public ProducerConsumerRunner(int producerThreadsCount, int consumerThreadsCount, int queueCapacity,
                                  ThroughputMetrics throughputMetrics,
                                  TaskProducer taskProducer, TaskConsumer taskConsumer) {
        this.throughputMetrics = throughputMetrics;
        taskQueue = new HalfBlockingQueue<>(queueCapacity);
        producerThreads = IntStream.range(0, producerThreadsCount)
                .mapToObj(i -> new TaskProducerThread("Producer-%d".formatted(i), taskQueue,
                        taskProducer))
                .collect(Collectors.toList());
        consumerThreads = IntStream.range(0, consumerThreadsCount)
                .mapToObj(i -> new TaskConsumerThread("Consumer-%d".formatted(i), taskQueue,
                        taskConsumer, this.throughputMetrics))
                .collect(Collectors.toList());
        logger.info("created %d producer(s) and %d consumer(s)".formatted(producerThreads.size(), consumerThreads.size()));
    }

    public void run() {
        producerThreads.forEach(TaskProducerThread::start);
        consumerThreads.forEach(TaskConsumerThread::start);

    }

    public void stop() {
        logger.info("interrupting runner...");
        producerThreads.forEach(Thread::interrupt);
        consumerThreads.forEach(Thread::interrupt);
        logger.info("interrupted");
    }
}
