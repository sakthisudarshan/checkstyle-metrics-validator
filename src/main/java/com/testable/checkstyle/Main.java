package com.testable.checkstyle;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.testable.checkstyle.checkstyle.CheckstyleRunner;
import com.testable.checkstyle.gate.CiCdGatekeeper;
import com.testable.checkstyle.metrics.MetricsEngine;
import com.testable.checkstyle.model.AuditSummary;
import com.testable.checkstyle.model.MetricResult;
import com.testable.checkstyle.report.AuditTrailReporter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Entry point: runs Checkstyle on sample sources and prints all 12 metric results.
 */
public final class Main {

    public static void main(String[] args) throws CheckstyleException, IOException {
        Path projectRoot = Paths.get("").toAbsolutePath();
        Path config = projectRoot.resolve("config/checkstyle.xml");
        Path suppressions = projectRoot.resolve("config/suppressions.xml");
        Path samples = projectRoot.resolve("src/test/resources/samples");

        CheckstyleRunner runner = new CheckstyleRunner(config, suppressions);
        AuditSummary summary = runner.auditDirectory(samples);

        MetricsEngine engine = new MetricsEngine();
        List<MetricResult> results = engine.evaluateAll(summary);

        System.out.println("=== Checkstyle Metrics Validation Report ===");
        System.out.println("Source directory: " + samples);
        System.out.println("Total violations: " + summary.getTotalViolations());
        System.out.println("Lines of code: " + summary.getLinesOfCode());
        System.out.println();

        for (MetricResult result : results) {
            System.out.printf("[%s] %s%n", result.isPassed() ? "PASS" : "FAIL", result);
        }

        CiCdGatekeeper gatekeeper = new CiCdGatekeeper(200, 0);
        CiCdGatekeeper.GateResult gate = gatekeeper.evaluate(summary);
        System.out.println();
        System.out.println("CI/CD Gate: " + (gate.passed() ? "PASS" : "FAIL") + " - " + gate.reason());

        Path reportPath = projectRoot.resolve("target/checkstyle-audit.xml");
        new AuditTrailReporter().writeXmlReport(summary.getViolations(), reportPath);
        System.out.println("Audit trail written to: " + reportPath);
    }
}
