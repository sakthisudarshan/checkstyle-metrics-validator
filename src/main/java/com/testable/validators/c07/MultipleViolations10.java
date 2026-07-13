package com.testable.validators.c07;

/**
 * Classification: Multiple Violations Detection
 * Metric: Aggregated Risk Assessment
 *
 * Expected: 10+ violations in a single file (hot-file threshold).
 */
class multiple_violations_10 {

    int bad_field = 1;

    void run(int X) {
        int z = 10;
        if (X > 0) return;
        int unused = 3;
        if (true) {
        }
        int a=1+2+3+4;
    }
}
