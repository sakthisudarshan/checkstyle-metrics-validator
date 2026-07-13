package com.testable.demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CleanDemoTest {

    @Test
    void getValueReturnsConstructorValue() {
        CleanDemo demo = new CleanDemo(7);
        assertEquals(7, demo.getValue());
    }
}
