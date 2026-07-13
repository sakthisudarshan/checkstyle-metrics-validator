# Checkstyle Metrics Validator (Java)

Validates all **12 Testable Lint / Rule Violation metrics** from the Strategy & Metrics Mapping spreadsheet using **Checkstyle** as the analysis tool. All implementation code is **Java only**.

## Metrics Covered

| # | Classification | Metric | Checkstyle Derivation |
|---|----------------|--------|----------------------|
| 1 | Rule Detection Test | Violation Density per KLOC | `violation_density = violations_count` |
| 2 | Unused Variable Detection | Resource Waste Identification | `unused_variable_violations = count(Unused)` |
| 3 | Naming Convention Validation | Semantic Consistency Score | `semantic_consistency = 1 / (1 + naming_violations)` |
| 4 | Code Style Rule Validation | Syntactic Uniformity Score | `syntactic_uniformity = 1 / (1 + style_violations)` |
| 5 | Complexity Rule Detection | Structural Threshold Monitoring | `structural_threshold = complexity_violations` |
| 6 | Rule Severity Classification | Impact Prioritization | `impact_priority = errors * 2 + warnings` |
| 7 | Multiple Violations Detection | Aggregated Risk Assessment | `aggregated_risk = violations_count` |
| 8 | False Positive Prevention | Accuracy Tuning | `accuracy_proxy = 1 / (1 + warnings)` + suppressions |
| 9 | Custom Rule Validation | Project-Specific Enforcement | `custom_rule_enforcement = custom_violations` |
| 10 | Configuration File Handling | Environment Standardization | consistent config → identical audit results |
| 11 | CI/CD Integration Validation | Automated Gatekeeping | `gatekeeping_score = errors` |
| 12 | Violation Reporting Validation | Quality Audit Trail | `audit_trail = violations_count` + field completeness |

## Project Structure

```
checkstyle/
├── config/
│   ├── checkstyle.xml          # Team-wide rule configuration
│   ├── checkstyle-ci.xml       # Stricter CI gate configuration
│   └── suppressions.xml        # False-positive suppression filter
├── src/main/java/
│   └── com/testable/checkstyle/
│       ├── Main.java                    # Run all metrics
│       ├── checkstyle/CheckstyleRunner  # Programmatic Checkstyle execution
│       ├── custom/ForbidSystemOutCheck  # Project-specific custom rule
│       ├── metrics/MetricsEngine        # All 12 metric calculators
│       ├── gate/CiCdGatekeeper          # Build gate logic
│       └── report/AuditTrailReporter    # XML audit trail output
├── src/test/resources/samples/          # Intentional violation samples
└── src/test/java/                       # JUnit validation for all metrics
```

## Prerequisites

- Java 17+
- Maven 3.8+

## Platform Integration (TESTABLE Tool List)

The TESTABLE platform discovers tools via the gate file at:

```
checkstyle/0/checkstyle.json
```

This file must exist with `"tool": "checkstyle"` for Checkstyle to appear in the tool list and trigger execution.

### Export platform gate

```bash
.\mvnw.cmd exec:java@export-platform
.\mvnw.cmd exec:java@validate-gate
```

S3 upload path: `s3://<bucket>/checkstyle/0/checkstyle.json`

## Quick Start

```bash
# Compile and run all metric validations
mvn test

# Print metric report to console
mvn -q exec:java -Dexec.mainClass=com.testable.checkstyle.Main
```

Or after packaging:

```bash
mvn package -DskipTests
java -jar target/checkstyle-metrics-validator-1.0.0-SNAPSHOT.jar
```

## How Each Metric Is Verified

1. **Violation Density** — Runs Checkstyle on sample sources, counts total violations, divides by KLOC.
2. **Unused Variables** — Detects `UnusedLocalVariable` rule hits in `UnusedVariableSample.java`.
3. **Naming Conventions** — Detects `*Name` check violations in `NamingViolationSample.java`.
4. **Code Style** — Detects indentation/whitespace/line-length issues in `StyleViolationSample.java`.
5. **Complexity** — Detects cyclomatic complexity, nesting depth, method length in `ComplexityViolationSample.java`.
6. **Severity Classification** — Buckets violations by error/warning/info severity levels.
7. **Aggregated Risk** — Counts per-file violation density in `MultipleViolationsSample.java`.
8. **False Positive Prevention** — Uses `suppressions.xml` to filter intentional violations.
9. **Custom Rules** — `ForbidSystemOutCheck` flags `System.out` in `CustomRuleViolationSample.java`.
10. **Config Standardization** — Runs the same config twice; results must be identical.
11. **CI/CD Gatekeeping** — `CiCdGatekeeper` passes/fails based on error and violation thresholds.
12. **Audit Trail** — Generates XML report with line, column, message, source, and severity fields.

## Sample Sources

Sample files under `src/test/resources/samples/` intentionally contain violations to prove each metric is measurable. `CleanSample.java` verifies zero-violation baseline.

## Configuration

Edit `config/checkstyle.xml` to change active rules. Edit `config/suppressions.xml` to tune false-positive filtering. Use `config/checkstyle-ci.xml` for stricter CI pipeline gates.

## Output

- Console metric report via `Main`
- XML audit trail at `target/checkstyle-audit.xml`
- JUnit test results confirming all 12 metrics execute correctly
