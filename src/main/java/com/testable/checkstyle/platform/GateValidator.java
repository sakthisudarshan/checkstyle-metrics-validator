package com.testable.checkstyle.platform;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class GateValidator {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private GateValidator() {
    }

    public static List<String> validate(Path platformFile, boolean requirePerfect) throws IOException {
        List<String> errors = new ArrayList<>();
        if (!Files.exists(platformFile)) {
            errors.add("platform file not found: " + platformFile);
            return errors;
        }

        JsonNode data = MAPPER.readTree(platformFile.toFile());
        if (data.path("exit").asInt(-1) != 0) {
            errors.add("exit must be 0, got " + data.path("exit"));
        }
        if (!data.path("scan_ok").asBoolean(false)) {
            errors.add("scan_ok must be true");
        }
        if (!MetricsConstants.TOOL.equals(data.path("tool").asText())) {
            errors.add("tool must be checkstyle");
        }

        for (String key : MetricsConstants.PLATFORM_SCORE_KEYS) {
            if (!data.has(key)) {
                errors.add("missing platform score key: " + key);
                continue;
            }
            JsonNode value = data.get(key);
            if (!value.isNumber()) {
                errors.add(key + " must be numeric");
                continue;
            }
            double numeric = value.asDouble();
            if (numeric <= 1.0) {
                errors.add(key + "=" + numeric + " looks like a 0-1 fraction; TESTABLE expects 0-100 scale");
            }
            if (requirePerfect && numeric < 100) {
                errors.add(key + "=" + numeric + " below required minimum 100");
            }
        }

        JsonNode metrics = data.path("metrics");
        if (!metrics.isArray()) {
            errors.add("metrics array missing from platform JSON");
            return errors;
        }

        for (String technique : MetricsConstants.TECHNIQUES) {
            JsonNode metric = findMetric(metrics, technique);
            if (metric == null) {
                errors.add("missing technique in metrics[]: " + technique);
                continue;
            }
            if (requirePerfect) {
                if (metric.path("value").asInt(-1) < 100) {
                    errors.add(technique + ": value below 100");
                }
                if (!"PASS".equals(metric.path("result").asText())) {
                    errors.add(technique + ": result must be PASS");
                }
            }
        }
        return errors;
    }

    private static JsonNode findMetric(JsonNode metrics, String classification) {
        for (JsonNode metric : metrics) {
            if (classification.equals(metric.path("classification").asText())) {
                return metric;
            }
        }
        return null;
    }
}
