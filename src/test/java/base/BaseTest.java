package base;

import Pages.CarbCalculatorPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;

public abstract class BaseTest {

    protected WebDriver driver;

    protected static String baseURL;
    protected static String browser;
    private static String implicitlyWait;
    private static String pageLoadTimeout;

    protected CarbCalculatorPage carbCalPage;

    protected static ArrayList<HashMap<String, String>> testinputs = null;

    protected final static Logger logger = Logger.getLogger(BaseTest.class.getName());

    public void loadTestProperties() {
        logger.info("Load overall test properties");
        Properties properties = new Properties();
        FileInputStream propFile;
        try {
            propFile = new FileInputStream("test-config.properties");
            properties.load(propFile);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }

        baseURL = properties.getProperty("test.baseURL");
        browser = properties.getProperty("test.browser");
        implicitlyWait = properties.getProperty("test.implicitlyWait");
        pageLoadTimeout = properties.getProperty("test.pageLoadTimeout");
    }

    public void launchBrowser() {
        logger.info("========Browser session Started========");
        switch (browser.toLowerCase()) {
            case "chrome":
                driver = new ChromeDriver();
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(implicitlyWait)));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Integer.parseInt(pageLoadTimeout)));
    }

    public void initializeUITest() {
        logger.info("base URL="+baseURL+" browser type="+browser+" implicit Wait="+implicitlyWait+" pageLoad Timeout="+pageLoadTimeout);
        if (driver == null)
            launchBrowser();
        carbCalPage = new CarbCalculatorPage(driver);
        navigateToHomePage();
    }

    protected static void loadTestCaseInput(String testInputFile) {
        logger.info("load test input from: " + testInputFile);
        testinputs = new ArrayList<>();
        Scanner scanner = null;

        try {
            File file = new File(testInputFile);

            scanner = new Scanner(file);
            while ( scanner.hasNextLine() ) {
                HashMap<String, String> arg = new HashMap<>();
                String line = scanner.nextLine();
                if (!line.isEmpty()) {
                    String[] nvPairs = line.split(",");
                    for (String nvPair: nvPairs) {
                        String name = nvPair.split("=")[0];
                        String value= nvPair.split("=")[1];
                        arg.put(name, value);
                        logger.info(name + "=" + value);
                    }
                    testinputs.add(arg);
                }
            }
        }
        catch( Exception e ){
            logger.severe("fail to load test case input");
        }
        if (scanner != null) scanner.close();
    }

    protected static HashMap<String, String> getTestdata(String testName) {
        for (HashMap<String, String> input: testinputs) {
            String tc = input.get("tc");
            if (tc != null && tc.equalsIgnoreCase(testName)) {
                logger.info("found test data for " + testName);
                return input;
            }
        }
        logger.info("test data not available for "+ testName);
        return null;
    }

    public void cleanUp() {
        logger.info("===== Browser Session End =====");
        try {
            if (driver != null) {
                driver.quit();
                logger.info("Driver quit successfully");
            } else {
                logger.warning("Driver is null — no browser session to quit.");
            }
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Driver failed to quit", e);
        } finally {
            driver = null;
        }
    }

    public void navigateToHomePage() {
        logger.info("refresh to the original home page: " + baseURL);
        driver.navigate().to(baseURL);
    }

    public String getBaseURL() {
        return baseURL;
    }
}
