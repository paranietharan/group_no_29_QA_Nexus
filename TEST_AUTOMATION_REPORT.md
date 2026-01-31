# How to Generate Test Automation Report

## Quick Start (Run in Order)

```powershell
# Step 1: Run tests to generate JSON
mvn clean test

# Step 2: Generate advanced HTML report
mvn -q exec:java@report

# Step 3: Start report server (keep this running)
mvn -q exec:java@report-serve

# Step 4: Open report in browser
Start-Process "http://localhost:8081/overview-features.html
```

## Report Locations

| Report Type | File Location |
|-------------|---------------|
| Basic HTML  | `target/cucumber-report.html` |
| JSON        | `target/cucumber-report.json` |
| XML         | `target/cucumber-report.xml` |
| Advanced    | `target/cucumber-advanced-report/cucumber-html-reports/overview-features.html` |

## Notes
- When you open the .html report, you may get 404 error