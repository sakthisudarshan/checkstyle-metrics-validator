package com.testable.checkstyle.platform;

import com.testable.checkstyle.checkstyle.CheckstyleRunner;
import com.testable.checkstyle.model.AuditSummary;
import com.testable.checkstyle.model.MetricResult;

import java.nio.file.Path;
import java.util.List;

/**
 * Builds supplemental audits used by the platform exporter.
 */
public final class ScoringAuditOrchestrator {

    public AuditSummary configComparisonAudit(Path projectRoot) throws Exception {
        Path cleanDemo = projectRoot.resolve("src/main/java/com/testable/demo/CleanDemo.java");
        CheckstyleRunner configRunner = new CheckstyleRunner(
                projectRoot.resolve("config/checkstyle.xml"),
                projectRoot.resolve("config/suppressions.xml"),
                projectRoot.resolve("config/suppressions-xpath.xml"));
        return configRunner.auditFiles(List.of(cleanDemo.toFile()));
    }

    static PlatformMetricResult toPlatformMetric(MetricResult result) {
        return new PlatformMetricResult(
                result.getClassification(),
                result.getMetricName(),
                result.getRawValue(),
                result.getNormalizedScore(),
                result.isPassed() ? "PASS" : "FAIL",
                derive(result.getMetricName()),
                result.getDetails());
    }

    private static String derive(String metricName) {
        return switch (metricName) {
            case "Violation Density per KLOC" -> "violation_density = violations_count";
            case "Resource Waste Identification" -> "unused_variable_violations = count(Unused)";
            case "Semantic Consistency Score" -> "semantic_consistency = 1 / (1 + naming_violations)";
            case "Syntactic Uniformity Score" -> "syntactic_uniformity = 1 / (1 + style_violations)";
            case "Structural Threshold Monitoring" -> "structural_threshold = complexity_violations";
            case "Impact Prioritization" -> "impact_priority = errors * 2 + warnings";
            case "Aggregated Risk Assessment" -> "aggregated_risk = violations_count";
            case "Accuracy Tuning" -> "accuracy_proxy = 1 / (1 + warnings)";
            case "Project-Specific Enforcement" -> "custom_rule_enforcement = custom_violations";
            case "Environment Standardization" -> "environment_uniformity = 1 / (1 + violations_count)";
            case "Automated Gatekeeping" -> "gatekeeping_score = errors";
            case "Quality Audit Trail" -> "audit_trail = violations_count";
            default -> metricName;
        };
    }
}
