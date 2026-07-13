package com.testable.validators.c09;

/**
 * Classification: Custom Rule Validation
 * Metric: Project-Specific Enforcement
 *
 * Expected: fails ForbidSystemOutCheck (System.out.println present).
 */
public final class CustomRuleFailValidator {

    public void log(String message) {
        System.out.println(message);
    }
}
