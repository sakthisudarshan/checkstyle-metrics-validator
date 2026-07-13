package com.testable.checkstyle.platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.LinkedHashMap;
import java.util.Map;

public final class PlatformJsonBuilder {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private PlatformJsonBuilder() {
    }

    public static ObjectNode buildPlatformJson(PlatformScanReport report, String reportPath) {
        Map<String, Integer> platformScores = new LinkedHashMap<>();
        for (PlatformMetricResult metric : report.metrics()) {
            String camel = MetricsConstants.METRIC_TO_PLATFORM_KEY.get(metric.classification());
            String metricSnake = MetricsConstants.metricSnakeKey(metric.classification());
            String techniqueSnake = MetricsConstants.techniqueSnakeKey(metric.technique());
            int score = (int) Math.round(metric.normalisedScore());
            platformScores.put(camel, score);
            platformScores.put(metricSnake, score);
            platformScores.put(techniqueSnake, score);
        }

        int overallScore = MetricsConstants.PLATFORM_SCORE_KEYS.stream()
                .mapToInt(key -> platformScores.getOrDefault(key, 0))
                .min()
                .orElse(0);

        ObjectNode platform = MAPPER.createObjectNode();
        platform.put("exit", report.allGatesPassed() ? 0 : 1);
        platform.put("scan_ok", true);
        platform.put("report_path", reportPath);
        platform.put("checkstyle_path", reportPath);
        platform.put("platform_file", MetricsConstants.PLATFORM_RELATIVE_PATH);
        platform.put("total_violations", report.totalViolations());
        platform.put("totalViolations", report.totalViolations());
        platform.put("lines_of_code", report.linesOfCode());
        platform.put("linesOfCode", report.linesOfCode());
        platform.put("error_count", report.errorCount());
        platform.put("warning_count", report.warningCount());
        platform.put("overall_score", overallScore);
        platform.put("gate_name", MetricsConstants.GATE_NAME);
        platform.put("tool", MetricsConstants.TOOL);
        platform.put("execution_status", "COMPLETED");
        platform.put("all_gates_passed", report.allGatesPassed());
        platform.put("scoring_policy", "Excel normalisation formulas from Checkstyle audit");
        platform.put("scoringPolicy", "Excel normalisation formulas from Checkstyle audit");

        platformScores.forEach(platform::put);
        for (String key : MetricsConstants.PLATFORM_SCORE_KEYS) {
            platform.put(key, platformScores.getOrDefault(key, 0));
        }

        ArrayNode metrics = platform.putArray("metrics");
        for (PlatformMetricResult metric : report.metrics()) {
            ObjectNode entry = metrics.addObject();
            entry.put("classification", metric.technique());
            entry.put("metric", metric.classification());
            entry.put("technique", metric.technique());
            entry.put("value", (int) Math.round(metric.normalisedScore()));
            entry.put("execution_status", "COMPLETED");
            entry.put("result", metric.result());
            entry.put("coverage", (int) Math.round(metric.normalisedScore()));
            entry.put("derivation", metric.derivation());
        }
        return platform;
    }
}
