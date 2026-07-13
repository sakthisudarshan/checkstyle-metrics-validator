package samples;

/**
 * Sample with unused local variables for Resource Waste Identification metric.
 */
public class UnusedVariableSample {

    public int calculate(int input) {
        int unusedValue = 42;
        int anotherUnused = input * 2;
        return input + 1;
    }
}
