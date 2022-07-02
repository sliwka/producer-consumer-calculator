package org.example.infrastructure.sequenceMatcher;

import org.example.infrastructure.sequenceMatcher.matcherItems.AnythingToTheEnd;
import org.example.infrastructure.sequenceMatcher.matcherItems.SequenceMatcherItem;
import org.example.infrastructure.sequenceMatcher.matcherItems.WeakIncreasingInRange;

import java.util.ArrayList;
import java.util.List;

public class SequenceMatcher {
    private final List<SequenceMatcherItem> items = new ArrayList<>();

    public SequenceMatcher() {
    }

    public SequenceMatcher matchWeakIncreasingInRange(int fromInclusive, int toInclusive) {
        items.add(new WeakIncreasingInRange(fromInclusive, toInclusive));
        return this;
    }

    public SequenceMatcher matchAnythingToTheEnd() {
        items.add(new AnythingToTheEnd());
        return this;
    }

    public boolean matchInPrefix(List<Integer> elements) {
        Cursor position = Cursor.INITIAL;
        for (SequenceMatcherItem sequenceMatcherItem : items) {
            if (position.isMatched()) {
                position = sequenceMatcherItem.match(elements, position.getIndex());
            } else {
                return false;
            }
        }
        return position.isMatchedAtEndOf(elements);
    }
}
