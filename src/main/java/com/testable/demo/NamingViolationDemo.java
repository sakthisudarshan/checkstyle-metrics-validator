package com.testable.demo;

/**
 * Naming convention violations for Checkstyle automation validation.
 */
class naming_violation_demo {

    private int BAD_FIELD;

    public void DoSomething(int X) {
        int local_Bad = X;
        System.out.println(local_Bad);
    }
}
