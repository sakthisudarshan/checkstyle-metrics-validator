package com.testable.validators.c01;

import java.util.*;

/**
 * Classification: Rule Detection Test
 * Metric: Violation Density per KLOC
 *
 * Expected rules: AvoidStarImport, IllegalImport (error severity)
 */
public final class ImportRuleValidator {

    private ImportRuleValidator() {
    }

    public static void starImportUsage(List<String> items) {
        for (String item : items) {
            System.out.println(item);
        }
    }
}
