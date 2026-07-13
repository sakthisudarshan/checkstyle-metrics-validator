package com.testable.checkstyle.platform;

public record PlatformMetricResult(
        String technique,
        String classification,
        double rawValue,
        double normalisedScore,
        String result,
        String derivation,
        String details
) {
}
