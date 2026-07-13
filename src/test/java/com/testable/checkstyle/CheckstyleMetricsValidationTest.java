package com.testable.checkstyle;

import com.testable.checkstyle.gate.CiCdGatekeeper;
import com.testable.checkstyle.metrics.MetricsEngine;
import com.testable.checkstyle.model.AuditSummary;
import com.testable.checkstyle.model.MetricResult;
import com.testable.checkstyle.report.AuditTrailReporter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Validates all 12 Testable Lint / Rule Violation metrics using Checkstyle.
 */
class CheckstyleMetricsValidationTest {

    private static AuditSummary auditSummary;
    private static MetricsEngine metricsEngine;

    @BeforeAll
    static void setUp() throws Exception {
        auditSummary = TestSupport.auditSamples();
        metricsEngine = new MetricsEngine();
        assertTrue(auditSummary.getTotalViolations() > 0,
                "Sample sources must produce Checkstyle violations");
        assertTrue(auditSummary.getLinesOfCode() > 0,
                "Sample sources must have measurable LOC");
    }

    @Test
    @DisplayName("M1: Rule Detection Test - Violation Density per KLOC")
    void violationDensityPerKloc() {
        MetricResult result = metricsEngine.violationDensity(auditSummary);

        assertEquals("Violation Density per KLOC", result.getMetricName());
        assertEquals("Rule Detection Test", result.getClassification());
        assertTrue(result.getRawValue() > 0, "Violation density must be measurable");
        assertTrue(result.getNormalizedScore() >= 0 && result.getNormalizedScore() <= 100);
    }

    @Test
    @DisplayName("M2: Unused Variable Detection - Resource Waste Identification")
    void unusedVariableDetection() {
        MetricResult result = metricsEngine.unusedVariableDetection(auditSummary);

        assertEquals("Resource Waste Identification", result.getMetricName());
        assertTrue(result.getRawValue() >= 1,
                "Samples include unused variables that Checkstyle must detect");
    }

    @Test
    @DisplayName("M3: Naming Convention Validation - Semantic Consistency Score")
    void namingConventionValidation() {
        MetricResult result = metricsEngine.namingConventionValidation(auditSummary);

        assertEquals("Semantic Consistency Score", result.getMetricName());
        assertTrue(result.getRawValue() > 0 && result.getRawValue() <= 1,
                "Semantic consistency must be a ratio between 0 and 1");
        assertTrue(result.getDetails().contains("namingViolations"));
    }

    @Test
    @DisplayName("M4: Code Style Rule Validation - Syntactic Uniformity Score")
    void codeStyleRuleValidation() {
        MetricResult result = metricsEngine.codeStyleValidation(auditSummary);

        assertEquals("Syntactic Uniformity Score", result.getMetricName());
        assertTrue(result.getRawValue() > 0 && result.getRawValue() <= 1);
        assertTrue(result.getDetails().contains("styleViolations"));
    }

    @Test
    @DisplayName("M5: Complexity Rule Detection - Structural Threshold Monitoring")
    void complexityRuleDetection() {
        MetricResult result = metricsEngine.complexityRuleDetection(auditSummary);

        assertEquals("Structural Threshold Monitoring", result.getMetricName());
        assertTrue(result.getRawValue() >= 1,
                "ComplexityViolationSample must trigger complexity rules");
    }

    @Test
    @DisplayName("M6: Rule Severity Classification - Impact Prioritization")
    void ruleSeverityClassification() {
        MetricResult result = metricsEngine.ruleSeverityClassification(auditSummary);

        assertEquals("Impact Prioritization", result.getMetricName());
        assertTrue(result.getRawValue() >= auditSummary.getWarningCount(),
                "Impact priority must weight errors and warnings");
        assertTrue(result.getDetails().contains("warnings"));
    }

    @Test
    @DisplayName("M7: Multiple Violations Detection - Aggregated Risk Assessment")
    void multipleViolationsDetection() {
        MetricResult result = metricsEngine.multipleViolationsDetection(auditSummary);

        assertEquals("Aggregated Risk Assessment", result.getMetricName());
        assertTrue(result.getRawValue() >= 5,
                "MultipleViolationsSample must aggregate multiple rule breaks");
    }

    @Test
    @DisplayName("M8: False Positive Prevention - Accuracy Tuning")
    void falsePositivePrevention() throws Exception {
        AuditSummary withoutSuppression = new com.testable.checkstyle.checkstyle.CheckstyleRunner(
                TestSupport.projectRoot().resolve("config/checkstyle-no-suppressions.xml"),
                null).auditDirectory(TestSupport.samplesDir());

        MetricResult withSuppression = metricsEngine.falsePositivePrevention(auditSummary, 1);
        MetricResult without = metricsEngine.falsePositivePrevention(withoutSuppression, 0);

        assertEquals("Accuracy Tuning", withSuppression.getMetricName());
        assertTrue(withSuppression.getRawValue() > 0 && withSuppression.getRawValue() <= 1);
        assertTrue(without.getRawValue() > 0 && without.getRawValue() <= 1);
        assertTrue(withSuppression.getDetails().contains("suppressed"));
    }

    @Test
    @DisplayName("M9: Custom Rule Validation - Project-Specific Enforcement")
    void customRuleValidation() {
        MetricResult result = metricsEngine.customRuleValidation(auditSummary);

        assertEquals("Project-Specific Enforcement", result.getMetricName());
        assertTrue(result.getRawValue() >= 1,
                "CustomRuleViolationSample must trigger ForbidSystemOutCheck");
        assertFalse(result.isPassed(), "Custom rule violations must be detected as failures");
    }

    @Test
    @DisplayName("M10: Configuration File Handling - Environment Standardization")
    void configurationFileHandling() throws Exception {
        AuditSummary firstRun = TestSupport.auditSamples();
        AuditSummary secondRun = TestSupport.auditSamples();

        MetricResult result = metricsEngine.configurationFileHandling(firstRun, secondRun);

        assertEquals("Environment Standardization", result.getMetricName());
        assertTrue(result.isPassed(), "Identical config must produce consistent audit results");
        assertEquals(1.0, result.getRawValue(), 0.001);
    }

    @Test
    @DisplayName("M11: CI/CD Integration Validation - Automated Gatekeeping")
    void ciCdIntegrationValidation() {
        CiCdGatekeeper gatekeeper = new CiCdGatekeeper(500, 0);
        CiCdGatekeeper.GateResult gate = gatekeeper.evaluate(auditSummary);
        MetricResult result = metricsEngine.ciCdIntegrationValidation(auditSummary, 500);

        assertEquals("Automated Gatekeeping", result.getMetricName());
        assertEquals(gate.passed(), result.isPassed());
        assertTrue(result.getDetails().contains("gate="));
    }

    @Test
    @DisplayName("M12: Violation Reporting Validation - Quality Audit Trail")
    void violationReportingValidation() throws Exception {
        Path reportPath = TestSupport.projectRoot()
                .resolve("target/test-checkstyle-audit.xml");
        AuditTrailReporter reporter = new AuditTrailReporter();
        reporter.writeXmlReport(auditSummary.getViolations(), reportPath);

        MetricResult result = metricsEngine.violationReportingValidation(auditSummary);

        assertEquals("Quality Audit Trail", result.getMetricName());
        assertTrue(Files.exists(reportPath), "XML audit trail must be generated");
        assertTrue(result.getNormalizedScore() >= 95.0,
                "Audit entries must include line, message, and source");
        assertTrue(result.isPassed());
    }

    @Test
    @DisplayName("All 12 metrics execute via Checkstyle engine")
    void allMetricsExecute() {
        List<MetricResult> results = metricsEngine.evaluateAll(auditSummary);

        assertEquals(12, results.size());
        Map<String, MetricResult> byName = metricsEngine.evaluateAllAsMap(auditSummary);
        assertEquals(12, byName.size());

        for (MetricResult result : results) {
            assertEquals("Lint / Rule Violations", result.getTechnique());
            assertNotNull(result.getMetricName());
            assertNotNull(result.getDetails());
        }
    }

    @Test
    @DisplayName("Clean sample produces zero violations")
    void cleanSampleHasNoViolations() throws Exception {
        AuditSummary clean = TestSupport.auditCleanSampleOnly();
        assertEquals(0, clean.getTotalViolations());
    }
}
