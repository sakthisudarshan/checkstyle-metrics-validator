package com.testable.demo;

/**
 * Clean baseline class used for configuration validation.
 */
public final class CleanDemo {

    private final int value;

    /**
     * Creates a demo value holder.
     *
     * @param value stored integer value
     */
    public CleanDemo(int value) {
        this.value = value;
    }

    /**
     * Returns the stored value.
     *
     * @return stored integer value
     */
    public int getValue() {
        return value;
    }
}
