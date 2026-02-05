# Generate Allure Report

1. Run tests to generate Allure results
```powershell
mvn clean test
```

2. Generate Allure report
```powershell
mvn allure:report
```

3. Serve Allure report (optional)
```powershell
mvn allure:serve
```