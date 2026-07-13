package com.testable.checkstyle;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.testable.checkstyle.checkstyle.CheckstyleRunner;
import com.testable.checkstyle.model.AuditSummary;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigHandlingTest {

    private static final Path ROOT = Path.of("").toAbsolutePath();
    private static final Path CLEAN = ROOT.resolve("src/main/java/com/testable/demo/CleanDemo.java");

    @Test
    void validConfigurationExecutes() throws Exception {
        AuditSummary summary = runner("checkstyle.xml").auditFiles(java.util.List.of(CLEAN.toFile()));
        assertNotNull(summary);
    }

    @Test
    void alternateConfigurationExecutes() throws Exception {
        AuditSummary summary = runner("config/checkstyle-alternate.xml").auditFiles(java.util.List.of(CLEAN.toFile()));
        assertEquals(0, summary.getTotalViolations());
    }

    @Test
    void googleStyleConfigurationExecutes() throws Exception {
        AuditSummary summary = runner("config/checkstyle-google.xml").auditFiles(java.util.List.of(CLEAN.toFile()));
        assertNotNull(summary);
    }

    @Test
    void sunStyleConfigurationExecutes() throws Exception {
        AuditSummary summary = runner("config/checkstyle-sun.xml").auditFiles(java.util.List.of(CLEAN.toFile()));
        assertNotNull(summary);
    }

    @Test
    void missingConfigurationFails() {
        assertThrows(Exception.class, () ->
                new CheckstyleRunner(ROOT.resolve("config/does-not-exist.xml"), null)
                        .auditFiles(java.util.List.of(CLEAN.toFile())));
    }

    @Test
    void invalidConfigurationFails() {
        assertThrows(CheckstyleException.class, () ->
                new CheckstyleRunner(ROOT.resolve("config/checkstyle-invalid.xml"), null)
                        .auditFiles(java.util.List.of(CLEAN.toFile())));
    }

    @Test
    void consistentRunsAcrossPrimaryConfigs() throws Exception {
        AuditSummary primary = runner("checkstyle.xml").auditFiles(java.util.List.of(CLEAN.toFile()));
        AuditSummary alternate = runner("config/checkstyle.xml").auditFiles(java.util.List.of(CLEAN.toFile()));
        assertEquals(primary.getTotalViolations(), alternate.getTotalViolations());
        assertEquals(0, primary.getTotalViolations());
    }

    private static CheckstyleRunner runner(String config) {
        return new CheckstyleRunner(
                ROOT.resolve(config),
                ROOT.resolve("checkstyle-suppressions.xml"),
                ROOT.resolve("config/suppressions-xpath.xml"));
    }
}
