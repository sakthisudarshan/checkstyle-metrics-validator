package com.testable.demo;

public class UnusedVariableDemo {

    public int calculate(int input) {
        int unusedValue = 42;
        int anotherUnused = input * 2;
        return input + 1;
    }
}
