package com.testable.checkstyle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class ViolationReportingTest {

    @Test
    void mavenXmlReportContainsAuditFields() throws Exception {
        Path xml = Path.of("target/checkstyle-result.xml");
        assumeTrue(Files.exists(xml), "Run mvn verify first");
        String content = Files.readString(xml);
        assertTrue(content.contains("<checkstyle"));
        assertTrue(content.contains("version="));
    }

    @Test
    @EnabledIf("coverageReportExists")
    void coverageXmlReportContainsViolationFields() throws Exception {
        Path xml = Path.of("target/checkstyle/rule-coverage-result.xml");
        String content = Files.readString(xml);
        assertTrue(content.contains("line="));
        assertTrue(content.contains("message="));
        assertTrue(content.contains("source="));
        assertTrue(content.contains("severity="));
    }

    @Test
    @EnabledIf("htmlReportExists")
    void htmlReportGenerated() throws Exception {
        Path html = Path.of("target/site/checkstyle.html");
        String content = Files.readString(html);
        assertTrue(content.toLowerCase().contains("checkstyle"));
    }

    static boolean coverageReportExists() {
        return Files.exists(Path.of("target/checkstyle/rule-coverage-result.xml"));
    }

    static boolean htmlReportExists() {
        return Files.exists(Path.of("target/site/checkstyle.html"));
    }
}
