package com.testable.checkstyle.gate;

import com.testable.checkstyle.model.AuditSummary;

/**
 * CI/CD gatekeeper that blocks merges when quality thresholds are exceeded.
 */
public final class CiCdGatekeeper {

    private final int maxAllowedViolations;
    private final int maxAllowedErrors;

    public CiCdGatekeeper(int maxAllowedViolations, int maxAllowedErrors) {
        this.maxAllowedViolations = maxAllowedViolations;
        this.maxAllowedErrors = maxAllowedErrors;
    }

    public GateResult evaluate(AuditSummary summary) {
        boolean pass = summary.getErrorCount() <= maxAllowedErrors
                && summary.getTotalViolations() <= maxAllowedViolations;
        String reason;
        if (pass) {
            reason = "Build passes quality gate";
        } else if (summary.getErrorCount() > maxAllowedErrors) {
            reason = "Error count " + summary.getErrorCount() + " exceeds limit " + maxAllowedErrors;
        } else {
            reason = "Violation count " + summary.getTotalViolations()
                    + " exceeds limit " + maxAllowedViolations;
        }
        return new GateResult(pass, reason, summary.getErrorCount(), summary.getTotalViolations());
    }

    public record GateResult(boolean passed, String reason, int errorCount, int totalViolations) {
    }
}
