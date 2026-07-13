// Classification: False Positive Prevention
// Metric: Accuracy Tuning
// Expected: partial suppression via suppressions.xml (LineLength only)
package coverage.c08;

public class PartiallySuppressedSample {
    public static void longLine() {
        String line = "This intentionally long literal should be suppressed for LineLength only while naming violations remain visible.";
        System.out.println(line);
    }

    int bad_field = 2;
}
