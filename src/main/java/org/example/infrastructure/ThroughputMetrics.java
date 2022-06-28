package org.example.infrastructure;

import java.util.concurrent.atomic.AtomicInteger;

public class ThroughputMetrics {

    private final AtomicInteger consumedTasksCount = new AtomicInteger(0);

    public void incrementConsumedTasksCount() {
        consumedTasksCount.incrementAndGet();
    }

    public int getConsumedTasksCount() {
        return consumedTasksCount.get();
    }
}
