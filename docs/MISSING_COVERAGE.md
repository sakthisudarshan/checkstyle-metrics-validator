# Missing Coverage Report

Generated after `mvn clean verify`. Re-run verify to refresh `docs/COVERAGE_MATRIX.md`.

## SARIF

Checkstyle 10.x does not emit SARIF natively. This repository documents SARIF as **not supported** and validates XML, HTML, and console reporting instead.

## Notes

- Maven scoring audit (`target/checkstyle-result.xml`) intentionally contains **zero violations** so the TESTABLE platform gate remains **100/100**.
- Full rule triggering is validated through `target/checkstyle/rule-coverage-result.xml` and `reports/rule-coverage.json`.
- If any classification shows `0` rules in `COVERAGE_MATRIX.md`, add a validator file under `src/main/java/com/testable/validators/` and re-run verify.

## Current status

Run:

```powershell
.\mvnw.cmd clean verify -Ptestable-pipeline
```

Then inspect:

- `docs/COVERAGE_MATRIX.md`
- `reports/rule-coverage.json`

All 12 classifications should appear under `triggered_classifications`.
