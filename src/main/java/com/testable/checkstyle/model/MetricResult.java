package com.testable.checkstyle.model;

/**
 * Result of a single Testable metric evaluation.
 */
public final class MetricResult {

    private final String technique;
    private final String classification;
    private final String metricName;
    private final double rawValue;
    private final double normalizedScore;
    private final String unit;
    private final boolean passed;
    private final String details;

    public MetricResult(String technique, String classification, String metricName,
                        double rawValue, double normalizedScore, String unit,
                        boolean passed, String details) {
        this.technique = technique;
        this.classification = classification;
        this.metricName = metricName;
        this.rawValue = rawValue;
        this.normalizedScore = normalizedScore;
        this.unit = unit;
        this.passed = passed;
        this.details = details;
    }

    public String getTechnique() {
        return technique;
    }

    public String getClassification() {
        return classification;
    }

    public String getMetricName() {
        return metricName;
    }

    public double getRawValue() {
        return rawValue;
    }

    public double getNormalizedScore() {
        return normalizedScore;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isPassed() {
        return passed;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return metricName + " raw=" + rawValue + " score=" + normalizedScore
                + " passed=" + passed + " (" + details + ")";
    }
}
