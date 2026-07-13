package samples;

/**
 * Sample with naming convention violations for Semantic Consistency Score metric.
 */
public class naming_violation_sample {

    private int BAD_FIELD;

    public void DoSomething(int X) {
        int local_Bad = X;
        System.out.println(local_Bad);
    }
}
