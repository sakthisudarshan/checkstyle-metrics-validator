package com.testable.checkstyle.metrics;

import com.testable.checkstyle.model.AuditSummary;
import com.testable.checkstyle.model.MetricResult;
import com.testable.checkstyle.model.ViolationRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Computes all 12 Testable Lint / Rule Violation metrics from a Checkstyle audit.
 */
public final class MetricsEngine {

    private static final String TECHNIQUE = "Lint / Rule Violations";

    public List<MetricResult> evaluateAll(AuditSummary summary) {
        return List.of(
                violationDensity(summary),
                unusedVariableDetection(summary),
                namingConventionValidation(summary),
                codeStyleValidation(summary),
                complexityRuleDetection(summary),
                ruleSeverityClassification(summary),
                multipleViolationsDetection(summary),
                falsePositivePrevention(summary),
                customRuleValidation(summary),
                configurationFileHandling(summary),
                ciCdIntegrationValidation(summary, 500),
                violationReportingValidation(summary));
    }

    /**
     * M1: Rule Detection Test - Violation Density per KLOC.
     * Derivation: violation_density = violations_count
     * Formula: TotalViolations / (LOC / 1000)
     */
    public MetricResult violationDensity(AuditSummary summary) {
        double kloc = Math.max(summary.getLinesOfCode() / 1000.0, 0.001);
        double density = summary.getTotalViolations() / kloc;
        double normalized = summary.getTotalViolations() == 0
                ? 100.0
                : Math.max(0, 100 - (summary.getErrorCount() * 5.0 + summary.getWarningCount()));
        boolean passed = summary.getErrorCount() == 0 && density < 10;
        return new MetricResult(TECHNIQUE, "Rule Detection Test", "Violation Density per KLOC",
                density, normalized, "violations/KLOC", passed,
                "errors=" + summary.getErrorCount() + ", warnings=" + summary.getWarningCount()
                        + ", loc=" + summary.getLinesOfCode());
    }

    /**
     * M2: Unused Variable Detection - Resource Waste Identification.
     * Derivation: unused_variable_violations = count(error where source contains "Unused")
     */
    public MetricResult unusedVariableDetection(AuditSummary summary) {
        List<ViolationRecord> unused = summary.violationsMatching(v ->
                v.sourceContains("Unused") || v.messageContains("unused"));
        double deadAllocationPercent = summary.getTotalViolations() == 0
                ? 0
                : (unused.size() * 100.0) / summary.getTotalViolations();
        double normalized = Math.max(0, 100 - (deadAllocationPercent * 0.5));
        boolean passed = deadAllocationPercent < 1.0;
        return new MetricResult(TECHNIQUE, "Unused Variable Detection", "Resource Waste Identification",
                unused.size(), normalized, "count", passed,
                "deadAllocationPercent=" + String.format("%.2f", deadAllocationPercent));
    }

    /**
     * M3: Naming Convention Validation - Semantic Consistency Score.
     * Derivation: semantic_consistency = 1 / (1 + naming_violations)
     */
    public MetricResult namingConventionValidation(AuditSummary summary) {
        List<ViolationRecord> naming = summary.violationsMatching(v ->
                v.sourceContains("Name") || v.messageContains("name"));
        double semanticConsistency = naming.isEmpty() ? 1.0 : 1.0 / (1.0 + naming.size());
        double conventionRate = summary.getTotalViolations() == 0
                ? 0
                : (naming.size() * 100.0) / summary.getTotalViolations();
        double normalized = summary.getTotalViolations() == 0
                ? 100.0
                : Math.max(0, 100 - (conventionRate * 0.25));
        boolean passed = conventionRate < 2.0;
        return new MetricResult(TECHNIQUE, "Naming Convention Validation", "Semantic Consistency Score",
                semanticConsistency, normalized, "ratio", passed,
                "namingViolations=" + naming.size());
    }

    /**
     * M4: Code Style Rule Validation - Syntactic Uniformity Score.
     * Derivation: syntactic_uniformity = 1 / (1 + indentation_issues)
     */
    public MetricResult codeStyleValidation(AuditSummary summary) {
        List<ViolationRecord> style = summary.violationsMatching(v ->
                v.sourceContains("Indentation")
                        || v.sourceContains("Whitespace")
                        || v.sourceContains("LineLength")
                        || v.messageContains("indent")
                        || v.messageContains("whitespace"));
        double uniformity = style.isEmpty() ? 1.0 : 1.0 / (1.0 + style.size());
        double kloc = Math.max(summary.getLinesOfCode() / 1000.0, 0.001);
        double styleDensity = style.size() / kloc;
        double normalized = summary.getTotalViolations() == 0
                ? 100.0
                : Math.max(0, 100 - (styleDensity * 10));
        boolean passed = styleDensity < 5;
        return new MetricResult(TECHNIQUE, "Code Style Rule Validation", "Syntactic Uniformity Score",
                uniformity, normalized, "ratio", passed,
                "styleViolations=" + style.size() + ", density=" + String.format("%.2f", styleDensity));
    }

    /**
     * M5: Complexity Rule Detection - Structural Threshold Monitoring.
     * Derivation: structural_threshold = violations_count (complexity-related)
     */
    public MetricResult complexityRuleDetection(AuditSummary summary) {
        List<ViolationRecord> complexity = summary.violationsMatching(v ->
                v.sourceContains("Complexity")
                        || v.sourceContains("NestedIfDepth")
                        || v.sourceContains("MethodLength")
                        || v.messageContains("complexity")
                        || v.messageContains("nested"));
        double breachCount = complexity.size();
        double normalized = complexity.isEmpty() ? 100.0 : Math.max(0, 100 - (breachCount * 10));
        boolean passed = breachCount == 0;
        return new MetricResult(TECHNIQUE, "Complexity Rule Detection", "Structural Threshold Monitoring",
                breachCount, normalized, "count", passed,
                "complexityBreaches=" + (int) breachCount);
    }

    /**
     * M6: Rule Severity Classification - Impact Prioritization.
     * Derivation: impact_priority = errors * 2 + warnings
     */
    public MetricResult ruleSeverityClassification(AuditSummary summary) {
        double impactPriority = summary.getErrorCount() * 2.0 + summary.getWarningCount();
        double severityScore = summary.getErrorCount() * 10.0
                + summary.getWarningCount() * 2.0
                + summary.getInfoCount() * 0.5;
        double normalized = summary.getTotalViolations() == 0
                ? 100.0
                : Math.max(0, 100 - severityScore);
        boolean passed = summary.getErrorCount() == 0;
        return new MetricResult(TECHNIQUE, "Rule Severity Classification", "Impact Prioritization",
                impactPriority, normalized, "weighted", passed,
                "errors=" + summary.getErrorCount() + ", warnings=" + summary.getWarningCount()
                        + ", infos=" + summary.getInfoCount());
    }

    /**
     * M7: Multiple Violations Detection - Aggregated Risk Assessment.
     * Derivation: aggregated_risk = violations_count (per-file concentration)
     */
    public MetricResult multipleViolationsDetection(AuditSummary summary) {
        Map<String, Long> perFile = summary.getViolations().stream()
                .collect(Collectors.groupingBy(ViolationRecord::getFileName, Collectors.counting()));
        long hotFiles = perFile.values().stream().filter(count -> count > 10).count();
        long amberFiles = perFile.values().stream()
                .filter(count -> count >= 5 && count <= 10).count();
        double hotfileScore = hotFiles * 15 + amberFiles * 5;
        double aggregatedRisk = summary.getTotalViolations();
        double normalized = summary.getTotalViolations() == 0
                ? 100.0
                : Math.max(0, 100 - hotfileScore);
        boolean passed = hotFiles == 0;
        return new MetricResult(TECHNIQUE, "Multiple Violations Detection", "Aggregated Risk Assessment",
                aggregatedRisk, normalized, "count", passed,
                "hotFiles=" + hotFiles + ", amberFiles=" + amberFiles);
    }

    /**
     * M8: False Positive Prevention - Accuracy Tuning.
     * Derivation: accuracy_proxy = 1 / (1 + warnings) with suppression awareness
     */
    public MetricResult falsePositivePrevention(AuditSummary summary, int suppressedCount) {
        double accuracyProxy = 1.0 / (1.0 + summary.getWarningCount());
        double falsePositiveRate = summary.getTotalViolations() == 0
                ? 0
                : (suppressedCount * 100.0) / (summary.getTotalViolations() + suppressedCount);
        double normalized = summary.getTotalViolations() == 0
                ? 100.0
                : Math.max(0, 100 - (falsePositiveRate * 5));
        boolean passed = falsePositiveRate < 10.0;
        return new MetricResult(TECHNIQUE, "False Positive Prevention", "Accuracy Tuning",
                accuracyProxy, normalized, "ratio", passed,
                "suppressed=" + suppressedCount + ", falsePositiveRate="
                        + String.format("%.2f", falsePositiveRate) + "%");
    }

    public MetricResult falsePositivePrevention(AuditSummary summary) {
        return falsePositivePrevention(summary, 0);
    }

    /**
     * M9: Custom Rule Validation - Project-Specific Enforcement.
     * Derivation: custom_rule_enforcement = violations_count (custom rule hits)
     */
    public MetricResult customRuleValidation(AuditSummary summary) {
        List<ViolationRecord> custom = summary.violationsMatching(v ->
                v.sourceContains("ForbidSystemOutCheck")
                        || v.messageContains("Project-specific"));
        double normalized = custom.isEmpty() ? 100.0 : 0.0;
        boolean passed = custom.isEmpty();
        return new MetricResult(TECHNIQUE, "Custom Rule Validation", "Project-Specific Enforcement",
                custom.size(), normalized, "count", passed,
                "customRuleViolations=" + custom.size());
    }

    /**
     * M10: Configuration File Handling - Environment Standardization.
     * Derivation: environment_uniformity = 1 / (1 + violations_count)
     */
    public MetricResult configurationFileHandling(AuditSummary firstRun, AuditSummary secondRun) {
        boolean sameViolationCount = firstRun.getTotalViolations() == secondRun.getTotalViolations();
        Set<String> firstSources = firstRun.getViolations().stream()
                .map(ViolationRecord::getSource).collect(Collectors.toSet());
        Set<String> secondSources = secondRun.getViolations().stream()
                .map(ViolationRecord::getSource).collect(Collectors.toSet());
        boolean sameRules = firstSources.equals(secondSources);
        double uniformity = 1.0 / (1.0 + Math.abs(firstRun.getTotalViolations()
                - secondRun.getTotalViolations()));
        double configDrift = (sameViolationCount && sameRules) ? 0 : 1;
        boolean passed = sameViolationCount && sameRules;
        double normalized = passed ? 100.0 : Math.max(0, 100 - (configDrift * 25));
        return new MetricResult(TECHNIQUE, "Configuration File Handling", "Environment Standardization",
                uniformity, normalized, "ratio", passed,
                "consistentRuns=" + passed + ", driftScore=" + configDrift);
    }

    public MetricResult configurationFileHandling(AuditSummary summary) {
        return configurationFileHandling(summary, summary);
    }

    /**
     * M11: CI/CD Integration Validation - Automated Gatekeeping.
     * Derivation: gatekeeping_score = errors
     */
    public MetricResult ciCdIntegrationValidation(AuditSummary summary, int maxAllowedViolations) {
        boolean gatePass = summary.getErrorCount() == 0
                && summary.getTotalViolations() <= maxAllowedViolations;
        double normalized = gatePass ? 100.0 : 0.0;
        return new MetricResult(TECHNIQUE, "CI/CD Integration Validation", "Automated Gatekeeping",
                summary.getErrorCount(), normalized, "errors", gatePass,
                "gate=" + (gatePass ? "PASS" : "FAIL") + ", maxAllowed=" + maxAllowedViolations);
    }

    public MetricResult ciCdIntegrationValidation(AuditSummary summary) {
        return ciCdIntegrationValidation(summary, 0);
    }

    /**
     * M12: Violation Reporting Validation - Quality Audit Trail.
     * Derivation: audit_trail = violations_count with field completeness check
     */
    public MetricResult violationReportingValidation(AuditSummary summary) {
        int complete = 0;
        for (ViolationRecord violation : summary.getViolations()) {
            if (violation.getLine() > 0
                    && violation.getMessage() != null && !violation.getMessage().isBlank()
                    && violation.getSource() != null && !violation.getSource().isBlank()
                    && violation.getFileName() != null && !violation.getFileName().isBlank()) {
                complete++;
            }
        }
        double coverage = summary.getTotalViolations() == 0
                ? 100.0
                : (complete * 100.0) / summary.getTotalViolations();
        boolean passed = coverage >= 95.0;
        return new MetricResult(TECHNIQUE, "Violation Reporting Validation", "Quality Audit Trail",
                summary.getTotalViolations(), coverage, "percent", passed,
                "completeEntries=" + complete + "/" + summary.getTotalViolations());
    }

    public Map<String, MetricResult> evaluateAllAsMap(AuditSummary summary) {
        Map<String, MetricResult> results = new HashMap<>();
        for (MetricResult result : evaluateAll(summary)) {
            results.put(result.getMetricName(), result);
        }
        return results;
    }
}
