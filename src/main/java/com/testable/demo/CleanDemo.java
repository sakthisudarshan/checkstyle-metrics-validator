package com.testable.demo;

/**
 * Clean baseline class used for configuration validation.
 */
public final class CleanDemo {

    private final int value;

    /**
     * Creates a demo value holder.
     *
     * @param initialValue stored integer value
     */
    public CleanDemo(int initialValue) {
        this.value = initialValue;
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
