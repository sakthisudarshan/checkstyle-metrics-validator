package com.testable.checkstyle.platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.testable.checkstyle.checkstyle.CheckstyleRunner;
import com.testable.checkstyle.model.AuditSummary;
import com.testable.checkstyle.model.ViolationRecord;
import com.testable.checkstyle.report.AuditTrailReporter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Audits intentional violation fixtures and publishes a rule-coverage report
 * separate from the Maven scoring audit.
 */
public final class RuleCoverageExporter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private RuleCoverageExporter() {
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of("").toAbsolutePath();
        Path samplesDir = root.resolve("src/test/resources/samples");
        Path demoDir = root.resolve("src/main/java/com/testable/demo");
        Path coverageXml = root.resolve("target/checkstyle/rule-coverage-result.xml");
        Path coverageJson = root.resolve("reports/rule-coverage.json");

        CheckstyleRunner sampleRunner = new CheckstyleRunner(
                root.resolve("config/checkstyle.xml"),
                root.resolve("config/suppressions.xml"));
        AuditSummary sampleSummary = sampleRunner.auditDirectory(samplesDir);

        CheckstyleRunner demoRunner = new CheckstyleRunner(
                root.resolve("checkstyle.xml"),
                root.resolve("config/suppressions.xml"));
        List<java.io.File> demoFiles = Files.list(demoDir)
                .filter(path -> path.getFileName().toString().endsWith("Demo.java"))
                .filter(path -> !path.getFileName().toString().equals("CleanDemo.java"))
                .map(Path::toFile)
                .toList();
        AuditSummary demoSummary = demoRunner.auditFiles(demoFiles);

        List<ViolationRecord> allViolations = Stream.concat(
                sampleSummary.getViolations().stream(),
                demoSummary.getViolations().stream()).toList();

        AuditTrailReporter reporter = new AuditTrailReporter();
        reporter.writeXmlReport(allViolations, coverageXml);

        Set<String> ruleSources = new LinkedHashSet<>();
        Set<String> classifications = new LinkedHashSet<>();
        for (ViolationRecord violation : allViolations) {
            ruleSources.add(shortRuleName(violation.getSource()));
            classifications.add(classifyRule(violation));
        }

        ObjectNode payload = MAPPER.createObjectNode();
        payload.put("tool", MetricsConstants.TOOL);
        payload.put("purpose", "rule-coverage");
        payload.put("total_violations", allViolations.size());
        payload.put("sample_violations", sampleSummary.getTotalViolations());
        payload.put("demo_violations", demoSummary.getTotalViolations());
        payload.put("coverage_xml", "target/checkstyle/rule-coverage-result.xml");

        ArrayNode rules = payload.putArray("triggered_rules");
        ruleSources.forEach(rules::add);

        ArrayNode categories = payload.putArray("triggered_classifications");
        classifications.forEach(categories::add);

        Files.createDirectories(coverageJson.getParent());
        MAPPER.writerWithDefaultPrettyPrinter().writeValue(coverageJson.toFile(), payload);

        System.out.println("Wrote rule coverage XML: " + coverageXml);
        System.out.println("Wrote rule coverage JSON: " + coverageJson);
        System.out.println("Rule coverage violations: " + allViolations.size());
        System.out.println("Triggered rule families: " + ruleSources.size());
    }

    private static String shortRuleName(String source) {
        if (source == null || source.isBlank()) {
            return "unknown";
        }
        int lastDot = source.lastIndexOf('.');
        return lastDot >= 0 ? source.substring(lastDot + 1) : source;
    }

    private static String classifyRule(ViolationRecord violation) {
        String source = violation.getSource() == null ? "" : violation.getSource();
        String message = violation.getMessage() == null ? "" : violation.getMessage().toLowerCase();

        if (source.contains("ForbidSystemOutCheck") || message.contains("project-specific")) {
            return "Custom Rule Validation";
        }
        if (source.contains("Unused") || message.contains("unused")) {
            return "Unused Variable Detection";
        }
        if (source.contains("Name") || message.contains("name")) {
            return "Naming Convention Validation";
        }
        if (source.contains("Indentation") || source.contains("Whitespace")
                || source.contains("LineLength") || message.contains("indent")
                || message.contains("whitespace")) {
            return "Code Style Rule Validation";
        }
        if (source.contains("Complexity") || source.contains("NestedIfDepth")
                || source.contains("MethodLength") || message.contains("complexity")
                || message.contains("nested")) {
            return "Complexity Rule Detection";
        }
        return "Rule Detection Test";
    }
}
