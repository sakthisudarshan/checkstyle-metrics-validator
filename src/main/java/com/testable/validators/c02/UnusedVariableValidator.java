package com.testable.validators.c02;

/**
 * Classification: Unused Variable Detection
 * Metric: Resource Waste Identification
 *
 * Expected rules: UnusedLocalVariable, RedundantModifier, HiddenField, FinalLocalVariable
 */
public final class UnusedVariableValidator {

    private final int seed;

    public UnusedVariableValidator(int seed) {
        this.seed = seed;
    }

    // Expected: UnusedLocalVariable
    public int compute(int input) {
        int unusedLocal = 42;
        int shadowed = input + seed;
        return shadowed;
    }

    // Expected: HiddenField on parameter seed
    public void report(int seed) {
        System.out.println(seed);
    }

    // Expected: RedundantModifier (final on interface method not applicable here; use static in inner)
    public static final class Inner {
        public final static int FLAG = 1;
    }
}
