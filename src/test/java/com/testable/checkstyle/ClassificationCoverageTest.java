package com.testable.checkstyle;

import com.testable.checkstyle.checkstyle.CheckstyleRunner;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassificationCoverageTest {

    @Test
    void coverageCorpusTriggersCoreClassifications() throws Exception {
        Path root = Path.of("").toAbsolutePath();
        CheckstyleRunner runner = new CheckstyleRunner(
                root.resolve("checkstyle.xml"),
                root.resolve("config/suppressions-coverage.xml"),
                root.resolve("config/suppressions-xpath.xml"));

        var summary = runner.auditDirectory(root.resolve("src/main/java/com/testable/validators"));

        Set<String> triggered = new HashSet<>();
        summary.getViolations().forEach(v -> triggered.add(classify(v.getSource())));

        assertTrue(summary.getTotalViolations() > 50, "Validator corpus must produce violations");
        assertTrue(triggered.contains("Rule Detection Test"));
        assertTrue(triggered.contains("Naming Convention Validation"));
        assertTrue(triggered.contains("Code Style Rule Validation"));
        assertTrue(triggered.contains("Complexity Rule Detection"));
        assertTrue(triggered.contains("Custom Rule Validation"));
    }

    private static String classify(String source) {
        if (source == null) {
            return "Rule Detection Test";
        }
        if (source.contains("ForbidSystemOut")) {
            return "Custom Rule Validation";
        }
        if (source.contains("Unused")) {
            return "Unused Variable Detection";
        }
        if (source.contains("Name")) {
            return "Naming Convention Validation";
        }
        if (source.contains("Whitespace") || source.contains("Curly") || source.contains("Wrap")
                || source.contains("EmptyBlock")) {
            return "Code Style Rule Validation";
        }
        if (source.contains("Complexity") || source.contains("NestedIfDepth")
                || source.contains("MethodLength") || source.contains("ParameterNumber")
                || source.contains("ExecutableStatementCount")) {
            return "Complexity Rule Detection";
        }
        return "Rule Detection Test";
    }
}
