#!/bin/bash

# Test Setup Script for Mayuravel QA Tests

set -e

echo "=== Mayuravel Test Setup ==="

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed. Please install Java 11 or higher."
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed. Please install Maven 3.6 or higher."
    exit 1
fi

# Configuration
API_URL="${API_BASE_URL:-http://localhost:8080}"
UI_URL="${UI_BASE_URL:-http://localhost:8080}"
BROWSER="${BROWSER:-chrome}"
HEADLESS="${HEADLESS:-false}"

echo "Configuration:"
echo "  API Base URL: $API_URL"
echo "  UI Base URL: $UI_URL"
echo "  Browser: $BROWSER"
echo "  Headless: $HEADLESS"

# Wait for application to start (if needed)
echo -n "Checking if application is running..."
max_attempts=30
attempt=1

while [ $attempt -le $max_attempts ]; do
    if curl -s -f "$API_URL/health" > /dev/null 2>&1; then
        echo " ✓ Application is running!"
        break
    elif curl -s -f "$API_URL/actuator/health" > /dev/null 2>&1; then
        echo " ✓ Application is running! (actuator endpoint)"
        break
    elif curl -s -f "$API_URL/" > /dev/null 2>&1; then
        echo " ✓ Application is running! (root endpoint)"
        break
    fi
    
    echo -n "."
    sleep 2
    attempt=$((attempt + 1))
done

if [ $attempt -gt $max_attempts ]; then
    echo ""
    echo "Error: Application is not running at $API_URL"
    echo "Please start the application before running tests."
    exit 1
fi

# Create screenshots directory
mkdir -p target/screenshots

# Run tests
echo ""
echo "Running tests..."

mvn test \
    -Dapi.baseUrl="$API_URL" \
    -Dui.baseUrl="$UI_URL" \
    -Dbrowser="$BROWSER" \
    -Dheadless="$HEADLESS" \
    -DdetailedLogging=true \
    -DtakeScreenshots=true \
    -Dtest="*Test"

# Generate report
echo ""
echo "Generating test report..."
mvn surefire-report:report

echo ""
echo "=== Test Execution Complete ==="
echo "Reports available at: target/site/surefire-report.html"
echo "Screenshots available at: target/screenshots/"