package org.example.infrastructure;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class HalfBlockingQueue<T>  {

    private static final Logger logger = Logger.getLogger(HalfBlockingQueue.class.getName());

    private final BlockingQueue<T> queue;
    private final int halfOfQueueCapacity;
    private final Object halfEmptyMonitor = new Object();

    public HalfBlockingQueue(int capacity) {
        queue = new ArrayBlockingQueue<>(capacity);
        halfOfQueueCapacity = capacity / 2;
    }

    public T take() throws InterruptedException {
        T element = queue.take();
        logger.info("TAKEN,   size=%d".formatted(queue.size()));
        notifyIfQueueIsAtLeastHalfEmpty();
        return element;
    }

    public void offer(T element) throws InterruptedException {
        notifyIfQueueIsAtLeastHalfEmpty();
        synchronized (this) {
            try {
                queue.add(element);
                logger.info("ADDED,   size=%d".formatted(queue.size()));
            } catch (IllegalStateException e) {
                logger.info("WAITING");
                waitForQueueBeingAtLeasHalfEmpty();
                queue.offer(element);
                logger.info("OFFERED, size=%d".formatted(queue.size()));
            }
        }
    }

    private void waitForQueueBeingAtLeasHalfEmpty() throws InterruptedException {
        synchronized (halfEmptyMonitor) {
            halfEmptyMonitor.wait();
        }
    }

    private void notifyIfQueueIsAtLeastHalfEmpty() {
        if (queue.size() <= halfOfQueueCapacity) {
            synchronized (halfEmptyMonitor) {
                halfEmptyMonitor.notify();
            }
        }
    }
}
