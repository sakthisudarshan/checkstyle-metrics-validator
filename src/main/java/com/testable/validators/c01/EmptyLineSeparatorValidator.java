package com.testable.validators.c01;

/**
 * Classification: Rule Detection Test
 * Metric: Violation Density per KLOC
 *
 * Expected rule: EmptyLineSeparator (missing blank line between members).
 */
public final class EmptyLineSeparatorValidator {
    private int first;
    private int second;
    public int sum() {
        return first + second;
    }
}
