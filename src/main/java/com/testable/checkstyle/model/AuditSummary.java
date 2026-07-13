package com.testable.checkstyle.model;

import java.util.Collections;
import java.util.List;

/**
 * Aggregated Checkstyle audit output for a set of source files.
 */
public final class AuditSummary {

    private final List<ViolationRecord> violations;
    private final int linesOfCode;
    private final int errorCount;
    private final int warningCount;
    private final int infoCount;

    public AuditSummary(List<ViolationRecord> violations, int linesOfCode) {
        this.violations = List.copyOf(violations);
        this.linesOfCode = linesOfCode;
        int errors = 0;
        int warnings = 0;
        int infos = 0;
        for (ViolationRecord violation : violations) {
            if (violation.isError()) {
                errors++;
            } else if (violation.isWarning()) {
                warnings++;
            } else if (violation.isInfo()) {
                infos++;
            }
        }
        this.errorCount = errors;
        this.warningCount = warnings;
        this.infoCount = infos;
    }

    public List<ViolationRecord> getViolations() {
        return violations;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    public int getTotalViolations() {
        return violations.size();
    }

    public int getErrorCount() {
        return errorCount;
    }

    public int getWarningCount() {
        return warningCount;
    }

    public int getInfoCount() {
        return infoCount;
    }

    public List<ViolationRecord> violationsMatching(java.util.function.Predicate<ViolationRecord> predicate) {
        return violations.stream().filter(predicate).toList();
    }

    public static AuditSummary empty() {
        return new AuditSummary(Collections.emptyList(), 0);
    }
}
