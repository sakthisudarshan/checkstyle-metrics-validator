package com.testable.validators.c01;

/**
 * Classification: Rule Detection Test
 * Metric: Violation Density per KLOC
 *
 * Expected rules: LineLength, FileTabCharacter, TrailingWhitespace (RegexpMultiline),
 * EmptyLineSeparator, IllegalToken (LABELED_STAT), IllegalImport, AvoidStarImport,
 * OneStatementPerLine, NeedBraces
 */
public final class RuleDetectionValidator {

    private RuleDetectionValidator() {
    }

    // Expected: LineLength violation
    public static void lineLengthViolation() {
        String longLine = "This line intentionally exceeds the configured maximum length to trigger the LineLength rule for rule detection coverage.";
        if (longLine.length() > 0) {
            System.out.println(longLine);
        }
    }

    // Expected: OneStatementPerLine + NeedBraces violations
    public static int compactStatements(int value) {
        if (value > 0) return value; int next = value + 1; return next;
    }

    // Expected: IllegalToken LABELED_STAT violation
    public static int labeledStatement(int input) {
        outer:
        for (int i = 0; i < input; i++) {
            if (i % 2 == 0) {
                return i;
            }
        }
        return -1;
    }
}
