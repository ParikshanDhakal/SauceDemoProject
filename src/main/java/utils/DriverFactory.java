package utils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Creates the browser for each test, based on the "browser" and "headless" settings.
 * Selenium Manager (built into Selenium 4) downloads the matching driver automatically.
 */
public class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        String browser = ConfigReader.get("browser").toLowerCase();
        boolean headless = ConfigReader.getBoolean("headless") || isRunningInCi();

        WebDriver driver;
        switch (browser) {
            case "firefox":
                driver = new FirefoxDriver(firefoxOptions(headless));
                break;
            case "edge":
                driver = new EdgeDriver(edgeOptions(headless));
                break;
            case "chrome":
                driver = new ChromeDriver(chromeOptions(headless));
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser in config.properties: " + browser);
        }

        // Explicit waits in BasePage handle timing, so the implicit wait stays at zero
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        if (headless) {
            driver.manage().window().setSize(new Dimension(1920, 1080));
        } else {
            driver.manage().window().maximize();
        }
        return driver;
    }

    private static boolean isRunningInCi() {
        // GitHub Actions (and most CI servers) set CI=true
        return "true".equalsIgnoreCase(System.getenv("CI"));
    }

    private static ChromeOptions chromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();

        // Stop Chrome's "save password" and "password found in a data breach" pop-ups,
        // which appear after logging in with the public demo password and block clicks
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--incognito");

        if (headless) {
            options.addArguments("--headless=new", "--window-size=1920,1080",
                    "--no-sandbox", "--disable-dev-shm-usage");
        }
        return options;
    }

    private static FirefoxOptions firefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
        }
        return options;
    }

    private static EdgeOptions edgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        return options;
    }
}
