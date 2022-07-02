package org.example.infrastructure.sequenceMatcher.matcherItems;

import org.example.infrastructure.sequenceMatcher.Cursor;

import java.util.List;

public interface SequenceMatcherItem {
    Cursor match(List<Integer> elements, int startIndex);
}
