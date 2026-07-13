package com.testable.checkstyle.platform;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

public final class ReportPublisher {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ReportPublisher() {
    }

    public static void publish(Path root, PlatformScanReport report, String reportPath) throws Exception {
        var platformReport = PlatformJsonBuilder.buildPlatformJson(report, reportPath);

        Path platformDir = root.resolve("checkstyle/0");
        Path platformFile = platformDir.resolve("checkstyle.json");
        Files.createDirectories(platformDir);
        MAPPER.writerWithDefaultPrettyPrinter().writeValue(platformFile.toFile(), platformReport);

        Path reportsDir = root.resolve("reports");
        Files.createDirectories(reportsDir);
        MAPPER.writerWithDefaultPrettyPrinter().writeValue(
                reportsDir.resolve("metrics-report.json").toFile(), platformReport);
        MAPPER.writerWithDefaultPrettyPrinter().writeValue(
                reportsDir.resolve("lint-gate.json").toFile(), platformReport);
        Files.writeString(reportsDir.resolve("metrics-report.md"), renderReport(report, platformReport));
    }

    private static String renderReport(PlatformScanReport report, com.fasterxml.jackson.databind.JsonNode platform) {
        StringBuilder builder = new StringBuilder();
        builder.append("# Checkstyle Lint Metrics Report\n\n");
        builder.append("| Field | Value |\n|---|---|\n");
        builder.append("| Tool | ").append(report.tool()).append(" |\n");
        builder.append("| Violations | ").append(report.totalViolations()).append(" |\n");
        builder.append("| LOC | ").append(report.linesOfCode()).append(" |\n");
        builder.append("| Overall Score | ").append(platform.path("overall_score").asInt()).append("/100 |\n");
        builder.append("| Report Path | `").append(platform.path("report_path").asText()).append("` |\n");
        builder.append("| Platform File | `").append(MetricsConstants.PLATFORM_RELATIVE_PATH).append("` |\n");
        builder.append("| Generated | ").append(Instant.now()).append(" |\n\n");
        builder.append("| Technique | Metric | Score | Result |\n|---|---|---:|---|\n");
        for (PlatformMetricResult metric : report.metrics()) {
            builder.append("| ")
                    .append(metric.technique()).append(" | ")
                    .append(metric.classification()).append(" | ")
                    .append((int) Math.round(metric.normalisedScore())).append(" | ")
                    .append(metric.result()).append(" |\n");
        }
        return builder.toString();
    }
}
