package com.testable.checkstyle.platform;

import com.testable.checkstyle.metrics.MetricsEngine;
import com.testable.checkstyle.model.AuditSummary;
import com.testable.checkstyle.model.MetricResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class PlatformExporter {

    private static final MetricsEngine ENGINE = new MetricsEngine();

    public static void main(String[] args) throws Exception {
        boolean failOnGate = List.of(args).contains("--fail-on-gate");
        Path root = Path.of("").toAbsolutePath();

        Path reportPath = findMavenReport(root);
        if (reportPath == null) {
            System.err.println("Missing Checkstyle XML report under target/");
            System.err.println("Run: mvnw clean verify");
            System.exit(2);
        }

        AuditSummary mavenSummary = CheckstyleResultParser.parse(reportPath, root);
        AuditSummary configComparison = new ScoringAuditOrchestrator().configComparisonAudit(root);
        PlatformScanReport report = buildReport(mavenSummary, configComparison);

        String relativeReport = root.relativize(reportPath).toString().replace('\\', '/');
        ReportPublisher.publish(root, report, relativeReport);

        System.out.println("Wrote platform gate: " + root.resolve(MetricsConstants.PLATFORM_RELATIVE_PATH));
        System.out.println("S3 target path: s3://<bucket>/" + MetricsConstants.PLATFORM_RELATIVE_PATH);
        System.out.println("Report path: " + relativeReport);
        System.out.println("Violations: " + report.totalViolations());
        System.out.println("All gates passed: " + report.allGatesPassed());

        if (failOnGate && !report.allGatesPassed()) {
            System.err.println("Checkstyle gate FAILED");
            System.exit(1);
        }
    }

    static PlatformScanReport buildReport(AuditSummary mavenSummary, AuditSummary configComparison) {
        List<PlatformMetricResult> metrics = new ArrayList<>();
        boolean allPassed = true;

        for (MetricResult result : ENGINE.evaluateAll(mavenSummary)) {
            MetricResult effective = result;
            if ("Environment Standardization".equals(result.getMetricName())) {
                effective = ENGINE.configurationFileHandling(mavenSummary, configComparison);
            }
            if (!effective.isPassed()) {
                allPassed = false;
            }
            metrics.add(ScoringAuditOrchestrator.toPlatformMetric(effective));
        }

        return new PlatformScanReport(
                MetricsConstants.TOOL,
                mavenSummary.getTotalViolations(),
                mavenSummary.getLinesOfCode(),
                mavenSummary.getErrorCount(),
                mavenSummary.getWarningCount(),
                allPassed,
                metrics);
    }

    static Path findMavenReport(Path root) {
        List<Path> candidates = List.of(
                root.resolve("target/checkstyle-result.xml"),
                root.resolve("target/checkstyle/checkstyle-result.xml"));
        for (Path candidate : candidates) {
            if (Files.exists(candidate)) {
                return candidate;
            }
        }
        return null;
    }
}
