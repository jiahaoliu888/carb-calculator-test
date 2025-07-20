# Veeva QA Engineer - Technical Screening #

# Instructions for running the tests 
### Java project with Selenium WebDriver, TestNG and Maven

## Test Environment
- Java: OpenJDK 21.0.5
- Maven: 3.9.9
- TestNG: 7.11.0
- Browser(tested version):
    - chrome: 138.0.7204.97 (default)
    - firefox: 140.0.4
    - edge: 138.0.3351.95

## Test Properties
test.properties

## Test Input Data
dataset: src/main/resources/dataset/CarbCalculatorTestData.txt


## Build and Run All Tests
mvn clean test

## Testng Report
target/surefire-reports/
##


### Jiahao Liu - 07/20/2025
