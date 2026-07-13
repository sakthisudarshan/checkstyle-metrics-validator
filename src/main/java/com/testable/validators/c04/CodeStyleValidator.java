package com.testable.validators.c04;

/**
 * Classification: Code Style Rule Validation
 * Metric: Syntactic Uniformity Score
 *
 * Expected rules: WhitespaceAround, WhitespaceAfter, NoWhitespaceBefore, LeftCurly, RightCurly,
 * SeparatorWrap, OperatorWrap, EmptyBlock
 */
public final class CodeStyleValidator {

    public int evaluate(int value)
    {   // LeftCurly / WhitespaceAround
        if(value>0){return value+1;} // multiple style violations
        try {
        } catch (RuntimeException ignored) {
        }
        return 0;
    }

    public void emptyBlock() {
        if (true) {
        }
    }
}
