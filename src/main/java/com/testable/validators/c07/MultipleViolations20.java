package com.testable.validators.c07;

/**
 * Classification: Multiple Violations Detection
 * Metric: Aggregated Risk Assessment
 *
 * Expected: 20+ violations in a single file.
 */
class multiple_violations_20 {

    static final int WRONG = 0;
    String BAD = "x";
    int f1;
    int f2;
    int f3;

    void m1(int A) {
    }

    void m2(int B) {
    }

    void m3() {
        int u1 = 1;
        int u2 = 2;
        int u3 = 3;
        if (true) {
        }
        if (false) {
        }
        int x=1+2;
        int y=3+4;
    }

    void m4() {
        for (int i = 0; i < 10; i++) {
            if (i > 5) {
                if (i > 7) {
                    System.out.println(i);
                }
            }
        }
    }
}
