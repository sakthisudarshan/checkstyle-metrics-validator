package com.testable.validators.c03;

/**
 * Classification: Naming Convention Validation
 * Metric: Semantic Consistency Score
 *
 * Expected rules: TypeName, MethodName, MemberName, LocalVariableName, ParameterName, ConstantName
 */
class naming_convention_validator {

    static final int BAD_CONSTANT = 1;
    int bad_field = 2;

    int BadLocal = 3;

    void DoWork(int BadParam) {
        int BadLocalVar = BadParam + bad_field;
        System.out.println(BadLocalVar);
    }
}
