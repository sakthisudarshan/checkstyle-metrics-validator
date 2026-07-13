package com.testable.checkstyle.checkstyle;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.testable.checkstyle.model.AuditSummary;
import com.testable.checkstyle.model.ViolationRecord;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * Runs Checkstyle programmatically and collects violations for metric analysis.
 */
public final class CheckstyleRunner {

    private final Path configPath;
    private final Path suppressionsPath;

    public CheckstyleRunner(Path configPath, Path suppressionsPath) {
        this.configPath = configPath;
        this.suppressionsPath = suppressionsPath;
    }

    public AuditSummary auditDirectory(Path sourceDirectory) throws CheckstyleException, IOException {
        List<File> javaFiles = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(sourceDirectory)) {
            paths.filter(path -> path.toString().endsWith(".java"))
                    .map(Path::toFile)
                    .forEach(javaFiles::add);
        }
        return auditFiles(javaFiles);
    }

    public AuditSummary auditFiles(List<File> javaFiles) throws CheckstyleException, IOException {
        List<ViolationRecord> violations = new ArrayList<>();
        int linesOfCode = 0;

        for (File file : javaFiles) {
            linesOfCode += countLinesOfCode(file.toPath());
        }

        Properties properties = new Properties();
        if (suppressionsPath != null && Files.exists(suppressionsPath)) {
            properties.setProperty("checkstyle.suppressions.file", suppressionsPath.toString());
        }

        Configuration configuration = ConfigurationLoader.loadConfiguration(
                configPath.toUri().toString(),
                new PropertiesExpander(properties));

        Checker checker = new Checker();
        checker.setModuleClassLoader(Thread.currentThread().getContextClassLoader());
        checker.configure(configuration);
        checker.addListener(new CollectingListener(violations));
        checker.process(javaFiles);
        checker.destroy();

        return new AuditSummary(violations, linesOfCode);
    }

    public static int countLinesOfCode(Path file) throws IOException {
        int count = 0;
        for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("//") && !trimmed.startsWith("/*")
                    && !trimmed.startsWith("*") && !trimmed.startsWith("*/")) {
                count++;
            }
        }
        return count;
    }

    private static final class CollectingListener implements AuditListener {

        private final List<ViolationRecord> violations;

        private CollectingListener(List<ViolationRecord> violations) {
            this.violations = violations;
        }

        @Override
        public void auditStarted(AuditEvent event) {
            // no-op
        }

        @Override
        public void auditFinished(AuditEvent event) {
            // no-op
        }

        @Override
        public void fileStarted(AuditEvent event) {
            // no-op
        }

        @Override
        public void fileFinished(AuditEvent event) {
            // no-op
        }

        @Override
        public void addError(AuditEvent event) {
            violations.add(new ViolationRecord(
                    event.getFileName(),
                    event.getLine(),
                    event.getColumn(),
                    event.getMessage(),
                    event.getSourceName(),
                    event.getSeverityLevel().getName()));
        }

        @Override
        public void addException(AuditEvent event, Throwable throwable) {
            violations.add(new ViolationRecord(
                    event.getFileName(),
                    event.getLine(),
                    event.getColumn(),
                    throwable.getMessage(),
                    "exception",
                    "error"));
        }
    }
}
