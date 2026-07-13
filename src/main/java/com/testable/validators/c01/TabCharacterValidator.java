package com.testable.validators.c01;

/**
 * Classification: Rule Detection Test
 * Metric: Violation Density per KLOC
 *
 * Expected rule: FileTabCharacter (this file contains literal tab characters).
 */
public final class TabCharacterValidator {

	public static int tabbedMethod(int value) {
		return value + 1;
	}
}
