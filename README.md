# SauceDemo UI Test Automation (Selenium + Java)

[![UI Tests](https://github.com/ParikshanDhakal/SauceDemoProject/actions/workflows/tests.yml/badge.svg)](https://github.com/ParikshanDhakal/SauceDemoProject/actions/workflows/tests.yml)

Automated UI tests for [SauceDemo](https://www.saucedemo.com), a demo online store built for practicing test automation.
The framework uses **Selenium WebDriver, Java, TestNG and Maven** with the **Page Object Model**, creates an **HTML report with screenshots of failures**, and runs on **GitHub Actions** on every push.

## What is tested

**Regression suite: 28 test cases** (22 test methods; two use TestNG data providers)

| Area | Tests | What they check |
|------|:----:|-----------------|
| Login | 9 | Valid login; 5 invalid logins (locked-out user, wrong password, unknown user, empty username, empty password) with exact error messages; logout; blocking the Products page without login |
| Products | 6 | All 6 products shown with names and prices; default A to Z order; sorting Z to A, price low to high, price high to low; product page opens with the matching price |
| Cart | 6 | Cart badge count when adding and removing; cart shows the right items and prices; removing inside the cart; items kept after Continue Shopping |
| Checkout | 7 | Full purchase to "Thank you for your order!"; 3 required-field errors; item total = sum of prices; tax = 8% and total = item total + tax; Cancel returns to the cart |

**Known-bugs suite: 5 test cases.** SauceDemo's `problem_user` account has bugs built in on purpose.
I documented 5 of them as bug reports in **[BUGS.md](BUGS.md)**, each with an automated check that fails while the bug exists.

## Project structure

```
src/main/java
├── base/BaseClass.java            # Opens/closes the browser, logs results, screenshots on failure
├── pages/                         # Page Object Model: one class per page
│   ├── BasePage.java              # Explicit waits + shared header (cart icon, menu, logout)
│   ├── LoginPage.java
│   ├── InventoryPage.java         # Products page
│   ├── ProductDetailsPage.java
│   ├── CartPage.java
│   ├── CheckoutInformationPage.java
│   ├── CheckoutOverviewPage.java
│   └── CheckoutCompletePage.java
├── utils/
│   ├── ConfigReader.java          # Reads config.properties (command-line values override it)
│   ├── DriverFactory.java         # Chrome / Firefox / Edge, normal or headless
│   ├── ExtentManager.java         # HTML report setup
│   └── TestData.java              # Users, products, checkout details
└── listeners/ExtentReportListener.java

src/test/java/tests
├── LoginTest.java
├── InventoryTest.java
├── CartTest.java
├── CheckoutTest.java
└── ProblemUserBugsTest.java       # Known bugs (expected to fail)

src/test/resources/config.properties
testng.xml                         # Regression suite
testng-known-bugs.xml              # Known-bugs suite
.github/workflows/tests.yml        # GitHub Actions pipeline
```

## How to run

You need Java 17+, Maven and Chrome. Selenium downloads the matching browser driver automatically.

```bash
# Regression suite in Chrome
mvn test

# Without opening a browser window
mvn test -Dheadless=true

# In Firefox or Edge
mvn test -Dbrowser=firefox

# Known-bugs suite (these tests are expected to fail)
mvn test -Dsuite=testng-known-bugs.xml
```

In Eclipse: **File → Import → Existing Maven Projects**, then right-click `testng.xml` → **Run As → TestNG Suite**.

## Test report

After a run, open `reports/ExtentReport.html` in a browser. Each test shows its description and result.
Failed tests include the error and a screenshot taken at the moment of failure (PNG copies are saved in `reports/screenshots/`).

On GitHub, open the **Actions** tab, pick a run, and download the report under **Artifacts**.

## Design choices

- **Page Object Model:** locators and page actions live in page classes, so tests read like user steps and a UI change only needs a fix in one place.
- **Explicit waits only:** every action waits for its element to be visible or clickable instead of using fixed sleeps or an implicit wait.
- **A fresh browser for every test:** tests do not depend on each other and can run in any order.
- **Data-driven tests:** invalid-login and checkout-validation cases come from TestNG `@DataProvider` tables, so adding a case is one line.
- **Config outside the code:** URL, browser, headless mode and timeout are in `config.properties` and can be overridden from the command line.
- **Continuous integration:** GitHub Actions runs both suites headless in Chrome on every push and pull request, and saves the HTML report.

## Tools

Java 17 · Selenium WebDriver 4 · TestNG · Maven · ExtentReports · GitHub Actions
