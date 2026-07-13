package com.testable.validators.c09;

/**
 * Classification: Custom Rule Validation
 * Metric: Project-Specific Enforcement
 *
 * Expected: passes ForbidSystemOutCheck (no System.out usage).
 */
public final class CustomRulePassValidator {

    private CustomRulePassValidator() {
    }

    public static String greet(String name) {
        return "hello-" + name;
    }
}
