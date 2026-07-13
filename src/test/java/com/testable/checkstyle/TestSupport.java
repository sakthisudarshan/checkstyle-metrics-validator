package com.testable.checkstyle;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.testable.checkstyle.checkstyle.CheckstyleRunner;
import com.testable.checkstyle.gate.CiCdGatekeeper;
import com.testable.checkstyle.metrics.MetricsEngine;
import com.testable.checkstyle.model.AuditSummary;
import com.testable.checkstyle.model.MetricResult;
import com.testable.checkstyle.report.AuditTrailReporter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Shared test fixture for running Checkstyle against sample sources.
 */
final class TestSupport {

    private static final Path PROJECT_ROOT = Paths.get("").toAbsolutePath();
    private static final Path CONFIG = PROJECT_ROOT.resolve("config/checkstyle.xml");
    private static final Path SUPPRESSIONS = PROJECT_ROOT.resolve("config/suppressions.xml");
    private static final Path SAMPLES = PROJECT_ROOT.resolve("src/test/resources/samples");

    private TestSupport() {
    }

    static CheckstyleRunner runner() {
        return new CheckstyleRunner(CONFIG, SUPPRESSIONS, PROJECT_ROOT.resolve("config/suppressions-xpath.xml"));
    }

    static Path samplesDir() {
        return SAMPLES;
    }

    static AuditSummary auditSamples() throws CheckstyleException, IOException {
        return runner().auditDirectory(SAMPLES);
    }

    static AuditSummary auditCleanSampleOnly() throws CheckstyleException, IOException {
        return runner().auditFiles(java.util.List.of(SAMPLES.resolve("CleanSample.java").toFile()));
    }

    static Path projectRoot() {
        return PROJECT_ROOT;
    }
}
