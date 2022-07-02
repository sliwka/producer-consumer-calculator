package org.example.infrastructure.sequenceMatcher.matcherItems;

import org.example.infrastructure.sequenceMatcher.Cursor;

import java.util.List;

public class AnythingToTheEnd implements SequenceMatcherItem {
    @Override
        public Cursor match(List<Integer> elements, int startIndex) {
        return new Cursor(elements.size(), true);
    }
}
