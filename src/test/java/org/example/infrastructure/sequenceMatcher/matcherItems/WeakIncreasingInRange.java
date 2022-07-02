package org.example.infrastructure.sequenceMatcher.matcherItems;

import org.example.infrastructure.sequenceMatcher.Cursor;

import java.util.List;

public class WeakIncreasingInRange implements SequenceMatcherItem {
    private final int fromInclusive;
    private final int toInclusive;

    public WeakIncreasingInRange(int fromInclusive, int toInclusive) {
        this.fromInclusive = fromInclusive;
        this.toInclusive = toInclusive;
    }

    @Override
    public Cursor match(List<Integer> elements, int startIndex) {
        int i = startIndex;
        if (elements.get(i) != fromInclusive) {
            return new Cursor(i, false);
        }
        i++;
        while (i < elements.size() && elements.get(i) >= elements.get(i - 1)) {
            i++;
        }
        return new Cursor(i, elements.get(i - 1) == toInclusive);
    }
}
