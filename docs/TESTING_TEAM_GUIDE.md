# Testing Team Guide — Checkstyle Lint Gate (100/100)

This repository is the **reference implementation** for TESTABLE **Lint / Rule Violations** metrics using **Checkstyle** on Java. All pipeline code is **Java only**.

## Repository

| Item | Value |
|------|-------|
| GitHub | https://github.com/sakthisudarshan/checkstyle-metrics-validator |
| Branch | `main` |
| Tool | `checkstyle` |
| Language | Java 17 |

## S3 platform path (required for trigger)

```text
checkstyle/0/checkstyle.json
```

## Maven report path (required for trigger)

```text
target/checkstyle-result.xml
```

The platform gate `report_path` must reference the Maven Checkstyle plugin XML output, not a source file.

## One-command validation

```powershell
git clone https://github.com/sakthisudarshan/checkstyle-metrics-validator.git
cd checkstyle-metrics-validator
.\scripts\run_checkstyle.ps1
```

**Expected final line:** `METRICS GATE VALIDATION: PASS (100/100)`

## Step-by-step validation

### 1. Run Checkstyle via Maven

```powershell
.\mvnw.cmd clean verify
```

Confirm:
- `target/checkstyle-result.xml` exists
- Maven reports `0 Checkstyle violations`

### 2. Export TESTABLE platform file

```powershell
.\mvnw.cmd exec:java@export-platform
.\mvnw.cmd exec:java@validate-gate
```

Or use the pipeline profile:

```powershell
.\mvnw.cmd clean verify -Ptestable-pipeline
```

## Files your review must check

| File | Purpose |
|------|---------|
| `checkstyle/0/checkstyle.json` | **Primary file** read by TESTABLE taxonomy gate |
| `target/checkstyle-result.xml` | Raw Maven Checkstyle output referenced by `report_path` |
| `reports/lint-gate.json` | Dashboard-format summary |
| `target/checkstyle/rule-coverage-result.xml` | Rule-coverage proof report (not used for scoring) |

### Required content in `checkstyle/0/checkstyle.json`

```json
{
  "exit": 0,
  "scan_ok": true,
  "tool": "checkstyle",
  "report_path": "target/checkstyle-result.xml",
  "checkstyle_path": "target/checkstyle-result.xml",
  "execution_status": "COMPLETED",
  "all_gates_passed": true
}
```

## Validation checklist

- [ ] `mvnw clean verify` exits with code 0
- [ ] `target/checkstyle-result.xml` exists after verify
- [ ] `checkstyle/0/checkstyle.json` exists with `"tool": "checkstyle"`
- [ ] `report_path` points to `target/checkstyle-result.xml`
- [ ] All 12 `metrics[].result` fields equal `"PASS"`
- [ ] GitHub Actions workflow is green on `main`

## Troubleshooting

| Symptom | Cause | Fix |
|---------|-------|-----|
| Tool not in TESTABLE list | Missing `checkstyle/0/checkstyle.json` | Run `mvnw clean verify` |
| Execution Status ❌ / N/A | Maven plugin not bound to `verify` | Check `pom.xml` maven-checkstyle-plugin |
| No `target/checkstyle-result.xml` | Plugin output path misconfigured | Verify `outputFile` in `pom.xml` |
| Gate export fails | Export ran before Checkstyle | Run full `mvnw clean verify` (plugin order fixed) |
| `report_path` points to `.java` file | Stale platform export | Re-run export after verify |
