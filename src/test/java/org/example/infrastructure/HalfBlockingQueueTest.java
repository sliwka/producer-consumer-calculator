package org.example.infrastructure;

import org.example.infrastructure.sequenceMatcher.SequenceMatcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class HalfBlockingQueueTest {

    private static final Logger logger = Logger.getLogger(HalfBlockingQueueTest.class.getName());

    @Test
    void testConcurrency() throws InterruptedException {
        // given
        final int capacity = 8;
        final int producerDurationMillis = 10;
        final int consumerDurationMillis = 30;
        final int testElementsCount = capacity * 4;
        var queue = new HalfBlockingQueue<Integer>(capacity);

        // when
        List<Integer> queueSizeHistory = new ArrayList<>();
        Thread producer = new Thread(() -> {
            for (int i = 0; i < testElementsCount; i++) {
                try {
                    queue.offer(i);
                    queueSizeHistory.add(queue.size());
                    Thread.sleep(producerDurationMillis);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < testElementsCount; i++) {
                try {
                    int element = queue.take();
                    Thread.sleep(consumerDurationMillis);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        producer.start();
        Thread.sleep(100);
        consumer.start();
        producer.join();
        consumer.join();

        // then
        Assertions.assertTrue(new SequenceMatcher()
                .matchWeakIncreasingInRange(1, capacity)
                .matchWeakIncreasingInRange(capacity / 2 + 1, capacity)
                .matchAnythingToTheEnd()
                .matchInPrefix(queueSizeHistory),
                "queue size history: " + queueSizeHistory);
        logger.info(queueSizeHistory.toString());
    }
}