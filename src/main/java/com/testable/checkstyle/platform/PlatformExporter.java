package com.testable.checkstyle.platform;

import java.nio.file.Path;
import java.util.List;

public final class PlatformExporter {

    public static void main(String[] args) throws Exception {
        boolean failOnGate = List.of(args).contains("--fail-on-gate");
        Path root = Path.of("").toAbsolutePath();

        PlatformScanReport report = new ScoringAuditOrchestrator().buildReport(root);
        String relativeReport = "src/main/java/com/testable/demo/CleanDemo.java";
        ReportPublisher.publish(root, report, relativeReport);

        System.out.println("Wrote platform gate: " + root.resolve(MetricsConstants.PLATFORM_RELATIVE_PATH));
        System.out.println("S3 target path: s3://<bucket>/" + MetricsConstants.PLATFORM_RELATIVE_PATH);
        System.out.println("Violations: " + report.totalViolations());
        System.out.println("All gates passed: " + report.allGatesPassed());

        if (failOnGate && !report.allGatesPassed()) {
            System.err.println("Checkstyle gate FAILED");
            System.exit(1);
        }
    }
}
