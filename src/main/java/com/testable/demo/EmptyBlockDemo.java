package com.testable.demo;

public class EmptyBlockDemo {

    public void emptyCatch() {
        try {
            int x = 1 / 0;
        } catch (ArithmeticException ignored) {
        }
    }

    public void emptyIf(int value) {
        if (value > 0) {
        }
    }
}
