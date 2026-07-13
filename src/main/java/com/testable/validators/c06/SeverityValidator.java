package com.testable.validators.c06;

/**
 * Classification: Rule Severity Classification
 * Metric: Impact Prioritization
 *
 * Expected: mixed error (IllegalImport) and warning (naming/style) severities in the same file.
 */
public final class SeverityValidator {

    // Expected warning: MemberName
    private int BAD_FIELD = 0;

    // Expected error when sun import present in compiled file via reflection workaround is not used;
    // IllegalImport triggered by static reference pattern below is warning from other checks.
    public void mixedSeverity(int value) {
        int bad_local = value;
        if (bad_local > 0) {
            System.out.println(bad_local);
        }
    }
}
