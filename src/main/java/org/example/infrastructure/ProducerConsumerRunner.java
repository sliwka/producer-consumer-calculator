package org.example.infrastructure;

import org.example.tasks.Task;
import org.example.tasks.TaskConsumer;
import org.example.tasks.TaskProducer;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProducerConsumerRunner {

    private static final Logger logger = Logger.getLogger(TaskConsumerThread.class.getName());

    private final BlockingQueue<Task> taskQueue;
    private final ThroughputMetrics throughputMetrics;
    private final List<TaskProducerThread> producerThreads;
    private final List<TaskConsumerThread> consumerThreads;

    public ProducerConsumerRunner(int producerThreadsCount, int consumerThreadsCount, int queueCapacity,
                                  TaskProducer taskProducer, TaskConsumer taskConsumer) {
        taskQueue = new ArrayBlockingQueue<>(queueCapacity);
        throughputMetrics = new ThroughputMetrics();
        producerThreads = IntStream.range(0, producerThreadsCount)
                .mapToObj(i -> new TaskProducerThread("Producer-%d".formatted(i), taskQueue,
                        taskProducer))
                .collect(Collectors.toList());
        consumerThreads = IntStream.range(0, consumerThreadsCount)
                .mapToObj(i -> new TaskConsumerThread("Consumer-%d".formatted(i), taskQueue,
                        taskConsumer, throughputMetrics))
                .collect(Collectors.toList());
        logger.info("created %d producer(s) and %d consumer(s)".formatted(producerThreads.size(), consumerThreads.size()));
    }

    public void run(int runDurationSeconds) {
        producerThreads.forEach(TaskProducerThread::start);
        consumerThreads.forEach(TaskConsumerThread::start);

        try {
            Thread.sleep(Duration.ofSeconds(runDurationSeconds).toMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        logger.info("throughput: %f tasks/s"
                .formatted((float)throughputMetrics.getConsumedTasksCount() / runDurationSeconds));

        logger.info("interrupting all...");
        producerThreads.forEach(Thread::interrupt);
        consumerThreads.forEach(Thread::interrupt);
        logger.info("interrupted");
    }
}
