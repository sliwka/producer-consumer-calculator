package org.example.infrastructure.sequenceMatcher;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Cursor {
    private final int index;
    private final boolean matched;
    public static final Cursor INITIAL = new Cursor(0, true);

    public boolean isMatchedAtEndOf(List<Integer> elements) {
        return matched && index == elements.size();
    }
}
