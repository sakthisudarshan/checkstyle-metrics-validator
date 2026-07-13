// Classification: Rule Severity Classification
// Metric: Impact Prioritization
// Expected: IllegalImport error severity (sun package)
package coverage.c06;

import sun.reflect.ReflectionFactory;

public class SeverityErrorSample {
    public void marker() {
        System.out.println(ReflectionFactory.class.getName());
    }
}
