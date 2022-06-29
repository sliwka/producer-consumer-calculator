package org.example.infrastructure;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class HalfBlockingQueue<T>  {

    private BlockingQueue<T> queue;

    public HalfBlockingQueue(int capacity) {
        queue = new ArrayBlockingQueue<>(capacity);
    }

    public void offer(T element) {
        queue.offer(element);
    }

    public T take() throws InterruptedException {
        return queue.take();
    }
}
