package com.testable.checkstyle.platform;

import java.util.List;

public record PlatformScanReport(
        String tool,
        int totalViolations,
        int linesOfCode,
        int errorCount,
        int warningCount,
        boolean allGatesPassed,
        List<PlatformMetricResult> metrics
) {
}
