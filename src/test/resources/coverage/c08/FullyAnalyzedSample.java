// Classification: False Positive Prevention
// Metric: Accuracy Tuning
// Expected: analyzed file with violations (no suppression)
package coverage.c08;

public class FullyAnalyzedSample {
    int BAD_FIELD = 1;

    void DoWork() {
        int unused = 0;
    }
}
