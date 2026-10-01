package base;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

import pages.InventoryPage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.DriverFactory;
import utils.ExtentManager;
import utils.TestData;

/**
 * Every test class extends this class.
 * Before each test: open a fresh browser on the login page and start a report entry.
 * After each test: record pass/fail/skip in the report, attach a screenshot if it failed, close the browser.
 */
public class BaseClass {

    protected WebDriver driver;
    protected ExtentTest extentTest;

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method, Object[] testData) {
        Test test = method.getAnnotation(Test.class);
        String description = (test != null) ? test.description() : "";
        extentTest = ExtentManager.getInstance()
                .createTest(reportName(method, testData), description)
                .assignCategory(method.getDeclaringClass().getSimpleName());

        driver = DriverFactory.createDriver();
        driver.get(ConfigReader.get("url"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.SUCCESS) {
                extentTest.pass("Test passed");
            } else if (result.getStatus() == ITestResult.FAILURE) {
                extentTest.fail(result.getThrowable());
                if (driver != null) {
                    attachScreenshot(result);
                }
            } else if (result.getStatus() == ITestResult.SKIP) {
                extentTest.skip(result.getThrowable() != null
                        ? result.getThrowable().getMessage() : "Test skipped");
            }
        } finally {
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        }
    }

    /** Opens the app as standard_user and returns the Products page. */
    protected InventoryPage loginAsStandardUser() {
        return new LoginPage(driver).loginSuccessfully(TestData.STANDARD_USER, TestData.PASSWORD);
    }

    /** Takes a screenshot, embeds it in the HTML report and saves a PNG in reports/screenshots. */
    private void attachScreenshot(ITestResult result) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

            Path folder = Paths.get(ExtentManager.REPORT_FOLDER, "screenshots");
            Files.createDirectories(folder);
            String fileName = result.getMethod().getMethodName() + "_" + System.currentTimeMillis() + ".png";
            Files.write(folder.resolve(fileName), screenshot);

            String base64 = Base64.getEncoder().encodeToString(screenshot);
            extentTest.fail("Screenshot at the moment of failure",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
        } catch (IOException | RuntimeException e) {
            extentTest.warning("Could not capture screenshot: " + e.getMessage());
        }
    }

    /** Report name like "LoginTest - invalidLoginShowsError [locked out user]" */
    private static String reportName(Method method, Object[] testData) {
        String name = method.getDeclaringClass().getSimpleName() + " - " + method.getName();
        if (testData != null && testData.length > 0) {
            name += " [" + testData[0] + "]";
        }
        return name;
    }
}
