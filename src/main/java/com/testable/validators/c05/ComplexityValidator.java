package com.testable.validators.c05;

/**
 * Classification: Complexity Rule Detection
 * Metric: Structural Threshold Monitoring
 *
 * Expected rules: CyclomaticComplexity, NPathComplexity, BooleanExpressionComplexity,
 * NestedIfDepth, MethodLength, ExecutableStatementCount, ParameterNumber
 */
public final class ComplexityValidator {

    public int analyze(int a, int b, int c, int d, int e, int f) {
        int score = 0;
        if (a > 0) {
            score++;
        }
        if (b > 0) {
            score++;
        }
        if (c > 0) {
            if (d > 0) {
                if (e > 0) {
                    score++;
                }
            }
        }
        if ((a > 0 && b > 0) || (c > 0 && d > 0) || (e > 0 && f > 0)) {
            score++;
        }
        if (a == 1) {
            score++;
        }
        if (a == 2) {
            score++;
        }
        if (a == 3) {
            score++;
        }
        if (a == 4) {
            score++;
        }
        if (a == 5) {
            score++;
        }
        if (a == 6) {
            score++;
        }
        if (a == 7) {
            score++;
        }
        if (a == 8) {
            score++;
        }
        if (a == 9) {
            score++;
        }
        if (a == 10) {
            score++;
        }
        return score;
    }
}
