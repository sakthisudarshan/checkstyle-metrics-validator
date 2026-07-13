// Classification: False Positive Prevention
// Metric: Accuracy Tuning
// Expected: fully suppressed file (no violations reported)
// checkstyle:off
package coverage.c08;

public class FullySuppressedSample {
    int bad_name = 1;
    void badMethod() {
        int unused = 0;
    }
}
// checkstyle:on
