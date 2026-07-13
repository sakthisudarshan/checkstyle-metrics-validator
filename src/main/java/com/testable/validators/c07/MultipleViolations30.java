package com.testable.validators.c07;

/**
 * Classification: Multiple Violations Detection
 * Metric: Aggregated Risk Assessment
 *
 * Expected: 30+ violations in a single file.
 */
class multiple_violations_30 {

    static final int C1 = 1;
    static final int C2 = 2;
    int m1;
    int m2;
    int m3;
    int m4;
    String s1;
    String s2;

    void a(int P1, int P2, int P3, int P4, int P5, int P6) {
        int l1 = 1;
        int l2 = 2;
        int l3 = 3;
        int l4 = 4;
        int l5 = 5;
        int l6 = 6;
        if (P1 > 0) {
        }
        if (P2 > 0) {
        }
        if (P3 > 0) {
        }
        if (true) return;
        int r=1+2+3;
        int q=4+5+6;
        if (P1 > 0 && P2 > 0 && P3 > 0) {
            System.out.println(r + q);
        }
    }

    void b() {
        outer:
        for (int i = 0; i < 3; i++) {
            if (i % 2 == 0) {
                continue;
            }
        }
    }

    void c() {
        try {
        } catch (RuntimeException e) {
        }
    }

    void d(int v) {
        if(v>0){System.out.println(v);}
    }
}
