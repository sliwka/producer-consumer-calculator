package org.example.infrastructure;

import org.example.infrastructure.sequenceMatcher.SequenceMatcher;
import org.example.tasks.InterruptableRunnable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class HalfBlockingQueueTest {

    public static final int ANY_CAPACITY = 8;
    public static final int SINGLE_QUEUE_OPERATION_TEST_TIMEOUT = 100;
    public static final int ANY_ELEMENT = 1;

    @Test
    void testOrder() throws InterruptedException {
        // given
        var queue = new HalfBlockingQueue<Integer>(ANY_CAPACITY);
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        // when/then
        Assertions.assertEquals(1, queue.take());
        Assertions.assertEquals(2, queue.take());
        Assertions.assertEquals(3, queue.take());
    }

    @Test
    void testTakeOnEmpty() {
        // given
        var queue = new HalfBlockingQueue<Integer>(ANY_CAPACITY);
        // when/then
        Assertions.assertTrue(isWaiting(queue::take));
    }

    @Test
    void testOfferOnEmpty() {
        // given
        var queue = new HalfBlockingQueue<Integer>(ANY_CAPACITY);
        // when/then
        Assertions.assertFalse(isWaiting(() -> queue.offer(ANY_ELEMENT)));
    }

    @Test
    void testTakeOnNonEmpty() throws InterruptedException {
        // given
        var queue = new HalfBlockingQueue<Integer>(ANY_CAPACITY);
        queue.offer(ANY_ELEMENT);
        // when/then
        Assertions.assertFalse(isWaiting(queue::take));
    }

    @Test
    void testOfferOnFull() throws InterruptedException {
        // given
        var queue = new HalfBlockingQueue<Integer>(ANY_CAPACITY);
        for (int i = 0; i < ANY_CAPACITY; i++) {
            queue.offer(ANY_ELEMENT);
        }
        // when/then
        Assertions.assertTrue(isWaiting(() -> queue.offer(ANY_ELEMENT)));
    }

    @Test
    void testTakeOnFull() throws InterruptedException {
        // given
        var queue = new HalfBlockingQueue<Integer>(ANY_CAPACITY);
        for (int i = 0; i < ANY_CAPACITY; i++) {
            queue.offer(ANY_ELEMENT);
        }
        // when/then
        Assertions.assertFalse(isWaiting(queue::take));
    }

    @Test
    void testConcurrency() throws InterruptedException {
        // given
        final int capacity = ANY_CAPACITY;
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
                    queue.take();
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
    }

    private boolean isWaiting(InterruptableRunnable o) {
        Thread thread = new Thread(() -> {
            try {
                o.run();
            } catch (InterruptedException e) {
                // ignore
            }
        });
        thread.start();
        try {
            Thread.sleep(HalfBlockingQueueTest.SINGLE_QUEUE_OPERATION_TEST_TIMEOUT);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (thread.isAlive()) {
            thread.interrupt();
            return true;
        } else {
            return false;
        }
    }
}