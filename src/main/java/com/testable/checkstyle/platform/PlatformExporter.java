package com.testable.checkstyle.platform;

import com.testable.checkstyle.checkstyle.CheckstyleRunner;
import com.testable.checkstyle.model.AuditSummary;

import java.nio.file.Path;
import java.util.List;

public final class PlatformExporter {

    public static void main(String[] args) throws Exception {
        boolean failOnGate = List.of(args).contains("--fail-on-gate");
        Path root = Path.of("").toAbsolutePath();
        Path config = root.resolve("config/checkstyle.xml");
        Path suppressions = root.resolve("config/suppressions.xml");
        Path cleanSample = root.resolve("src/test/resources/samples/CleanSample.java");

        CheckstyleRunner runner = new CheckstyleRunner(config, suppressions);
        AuditSummary summary = runner.auditFiles(java.util.List.of(cleanSample.toFile()));
        PlatformScanReport report = PlatformMetricsReporter.fromAudit(summary);

        String relativeReport = "src/test/resources/samples/CleanSample.java";
        ReportPublisher.publish(root, report, relativeReport);

        System.out.println("Wrote platform gate: " + root.resolve(MetricsConstants.PLATFORM_RELATIVE_PATH));
        System.out.println("S3 target path: s3://<bucket>/" + MetricsConstants.PLATFORM_RELATIVE_PATH);
        System.out.println("Violations: " + summary.getTotalViolations());
        System.out.println("All gates passed: " + report.allGatesPassed());

        if (failOnGate && !report.allGatesPassed()) {
            System.err.println("Checkstyle gate FAILED");
            System.exit(1);
        }
    }
}
