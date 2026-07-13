package com.testable.checkstyle.report;

import com.testable.checkstyle.model.ViolationRecord;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

/**
 * Generates XML audit trail reports for Checkstyle violations.
 */
public final class AuditTrailReporter {

    public Path writeXmlReport(List<ViolationRecord> violations, Path outputFile) throws IOException {
        Files.createDirectories(outputFile.getParent());
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<checkstyle version=\"10.21.4\">\n");
        xml.append("  <audit timestamp=\"").append(Instant.now()).append("\">\n");

        String currentFile = "";
        for (ViolationRecord violation : violations) {
            if (!violation.getFileName().equals(currentFile)) {
                if (!currentFile.isEmpty()) {
                    xml.append("  </file>\n");
                }
                currentFile = violation.getFileName();
                xml.append("  <file name=\"").append(escapeXml(currentFile)).append("\">\n");
            }
            xml.append("    <error line=\"").append(violation.getLine()).append("\"");
            xml.append(" column=\"").append(violation.getColumn()).append("\"");
            xml.append(" severity=\"").append(escapeXml(violation.getSeverity())).append("\"");
            xml.append(" message=\"").append(escapeXml(violation.getMessage())).append("\"");
            xml.append(" source=\"").append(escapeXml(violation.getSource())).append("\"/>\n");
        }
        if (!currentFile.isEmpty()) {
            xml.append("  </file>\n");
        }
        xml.append("  </audit>\n");
        xml.append("</checkstyle>\n");

        Files.writeString(outputFile, xml.toString(), StandardCharsets.UTF_8);
        return outputFile;
    }

    public int countCompleteEntries(List<ViolationRecord> violations) {
        int complete = 0;
        for (ViolationRecord violation : violations) {
            if (violation.getLine() > 0
                    && violation.getMessage() != null && !violation.getMessage().isBlank()
                    && violation.getSource() != null && !violation.getSource().isBlank()) {
                complete++;
            }
        }
        return complete;
    }

    private static String escapeXml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
