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

## Run Individual Test cases
# Run the Sales feature file
```powershell
mvn test "-Dcucumber.features=src/test/resources/features/auth_login.feature"
```

# Run a single scenario by tag (example: @AUTH-API-ADMIN-01 @positive @admin Login with valid admin credentials)
```powershell
mvn test "-Dcucumber.filter.tags=@AUTH-API-ADMIN-01"
```

# Run by tag (examples)
mvn test "-Dcucumber.filter.tags=@auth"
```


## Report Locations

| Report Type | File Location |
|-------------|---------------|
| Basic HTML  | `target/cucumber-report.html` |
| JSON        | `target/cucumber-report.json` |
| XML         | `target/cucumber-report.xml` |
| Advanced    | `target/cucumber-advanced-report/cucumber-html-reports/overview-features.html` |

## Notes
- When you open the .html report, you may get 404 error. So follow the steps to run the report server.