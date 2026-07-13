package com.testable.checkstyle.platform;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class MetricsConstants {

    public static final String GATE_NAME = "Checkstyle Lint / Rule Violations Gate";
    public static final String TOOL = "checkstyle";
    public static final String PLATFORM_RELATIVE_PATH = "checkstyle/0/checkstyle.json";

    public static final List<String> METRICS = List.of(
            "Violation Density per KLOC",
            "Resource Waste Identification",
            "Semantic Consistency Score",
            "Syntactic Uniformity Score",
            "Structural Threshold Monitoring",
            "Impact Prioritization",
            "Aggregated Risk Assessment",
            "Accuracy Tuning",
            "Project-Specific Enforcement",
            "Environment Standardization",
            "Automated Gatekeeping",
            "Quality Audit Trail"
    );

    public static final List<String> TECHNIQUES = List.of(
            "Rule Detection Test",
            "Unused Variable Detection",
            "Naming Convention Validation",
            "Code Style Rule Validation",
            "Complexity Rule Detection",
            "Rule Severity Classification",
            "Multiple Violations Detection",
            "False Positive Prevention",
            "Custom Rule Validation",
            "Configuration File Handling",
            "CI/CD Integration Validation",
            "Violation Reporting Validation"
    );

    public static final List<String> PLATFORM_SCORE_KEYS = List.of(
            "ViolationDensityPerKloc",
            "ResourceWasteIdentification",
            "SemanticConsistencyScore",
            "SyntacticUniformityScore",
            "StructuralThresholdMonitoring",
            "ImpactPrioritization",
            "AggregatedRiskAssessment",
            "AccuracyTuning",
            "ProjectSpecificEnforcement",
            "EnvironmentStandardization",
            "AutomatedGatekeeping",
            "QualityAuditTrail"
    );

    public static final Map<String, String> METRIC_TO_PLATFORM_KEY = buildMetricMap();
    public static final Map<String, String> TECHNIQUE_TO_METRIC = buildTechniqueMap();

    private MetricsConstants() {
    }

    private static Map<String, String> buildMetricMap() {
        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < METRICS.size(); i++) {
            map.put(METRICS.get(i), PLATFORM_SCORE_KEYS.get(i));
        }
        return Map.copyOf(map);
    }

    private static Map<String, String> buildTechniqueMap() {
        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < TECHNIQUES.size(); i++) {
            map.put(TECHNIQUES.get(i), METRICS.get(i));
        }
        return Map.copyOf(map);
    }

    public static String metricSnakeKey(String metric) {
        return switch (metric) {
            case "Violation Density per KLOC" -> "violation_density_per_kloc";
            case "Resource Waste Identification" -> "resource_waste_identification";
            case "Semantic Consistency Score" -> "semantic_consistency_score";
            case "Syntactic Uniformity Score" -> "syntactic_uniformity_score";
            case "Structural Threshold Monitoring" -> "structural_threshold_monitoring";
            case "Impact Prioritization" -> "impact_prioritization";
            case "Aggregated Risk Assessment" -> "aggregated_risk_assessment";
            case "Accuracy Tuning" -> "accuracy_tuning";
            case "Project-Specific Enforcement" -> "project_specific_enforcement";
            case "Environment Standardization" -> "environment_standardization";
            case "Automated Gatekeeping" -> "automated_gatekeeping";
            case "Quality Audit Trail" -> "quality_audit_trail";
            default -> metric.toLowerCase().replace(' ', '_');
        };
    }

    public static String techniqueSnakeKey(String technique) {
        return switch (technique) {
            case "Rule Detection Test" -> "rule_detection_test";
            case "Unused Variable Detection" -> "unused_variable_detection";
            case "Naming Convention Validation" -> "naming_convention_validation";
            case "Code Style Rule Validation" -> "code_style_rule_validation";
            case "Complexity Rule Detection" -> "complexity_rule_detection";
            case "Rule Severity Classification" -> "rule_severity_classification";
            case "Multiple Violations Detection" -> "multiple_violations_detection";
            case "False Positive Prevention" -> "false_positive_prevention";
            case "Custom Rule Validation" -> "custom_rule_validation";
            case "Configuration File Handling" -> "configuration_file_handling";
            case "CI/CD Integration Validation" -> "ci_cd_integration_validation";
            case "Violation Reporting Validation" -> "violation_reporting_validation";
            default -> technique.toLowerCase().replace(' ', '_');
        };
    }
}
