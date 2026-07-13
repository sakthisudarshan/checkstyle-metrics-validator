# Rule-to-File Mapping

| Rule / Scenario | Validator File | Classification |
|---|---|---|
| LineLength | `c01/RuleDetectionValidator.java` | Rule Detection Test |
| FileTabCharacter | `c01/TabCharacterValidator.java` | Rule Detection Test |
| TrailingWhitespace | `c01/TrailingWhitespaceValidator.java` | Rule Detection Test |
| EmptyLineSeparator | `c01/EmptyLineSeparatorValidator.java` | Rule Detection Test |
| IllegalToken (LABELED_STAT) | `c01/RuleDetectionValidator.java` | Rule Detection Test |
| IllegalImport / AvoidStarImport | `c01/ImportRuleValidator.java` | Rule Detection Test |
| OneStatementPerLine / NeedBraces | `c01/RuleDetectionValidator.java` | Rule Detection Test |
| UnusedLocalVariable / RedundantModifier / HiddenField | `c02/UnusedVariableValidator.java` | Unused Variable Detection |
| UnusedImports | `c02/UnusedImportValidator.java` | Unused Variable Detection |
| TypeName / MethodName / MemberName / LocalVariableName / ParameterName / ConstantName | `c03/NamingConventionValidator.java` | Naming Convention Validation |
| WhitespaceAround / WhitespaceAfter / NoWhitespaceBefore / LeftCurly / RightCurly / OperatorWrap / EmptyBlock | `c04/CodeStyleValidator.java` | Code Style Rule Validation |
| CyclomaticComplexity / NPathComplexity / BooleanExpressionComplexity / NestedIfDepth / MethodLength / ExecutableStatementCount / ParameterNumber | `c05/ComplexityValidator.java` | Complexity Rule Detection |
| IllegalImport error + naming warnings | `c06/SeverityValidator.java`, `coverage/c06/SeverityErrorSample.java` | Rule Severity Classification |
| 10+ / 20+ / 30+ violations per file | `c07/MultipleViolations10/20/30.java` | Multiple Violations Detection |
| SuppressionFilter / Xpath / Comment | `coverage/c08/*.java`, `config/suppressions-coverage.xml` | False Positive Prevention |
| ForbidSystemOutCheck pass/fail | `c09/CustomRulePassValidator.java`, `c09/CustomRuleFailValidator.java` | Custom Rule Validation |
| Primary / alternate / google / sun / invalid configs | `config/checkstyle*.xml`, `ConfigHandlingTest.java` | Configuration File Handling |
| Maven verify + GitHub workflow | `pom.xml`, `.github/workflows/checkstyle.yml` | CI/CD Integration Validation |
| XML / HTML / console reports | `target/checkstyle-result.xml`, `target/site/checkstyle.html` | Violation Reporting Validation |
