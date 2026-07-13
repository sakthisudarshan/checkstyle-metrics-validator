package samples;

/**
 * Sample with complexity violations for Structural Threshold Monitoring metric.
 */
public class ComplexityViolationSample {

    public int complexMethod(int a, int b, int c, int d) {
        int result = 0;
        if (a > 0) {
            if (b > 0) {
                if (c > 0) {
                    if (d > 0) {
                        result = a + b + c + d;
                    } else if (d < 0) {
                        result = a + b + c - d;
                    } else {
                        result = a + b;
                    }
                } else {
                    result = a;
                }
            } else {
                result = b;
            }
        } else if (a < 0) {
            result = -a;
        } else {
            result = 0;
        }
        if (b > 10) {
            result += b;
        }
        if (c > 10) {
            result += c;
        }
        if (d > 10) {
            result += d;
        }
        return result;
    }
}
