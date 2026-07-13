package samples;

/**
 * Sample where a violation is intentionally suppressed via suppressions.xml.
 */
public class SuppressedViolationSample {

    public void methodWithLongLineThatWouldNormallyTriggerLineLengthCheckButIsSuppressed() {
        int value = 1;
        System.out.println(value);
    }
}
