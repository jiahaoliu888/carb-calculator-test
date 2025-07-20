package Common;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public abstract class BasePage {

    protected final static Logger logger = Logger.getLogger(BasePage.class.getName());
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    protected void type(WebElement element, String text){
        waitForElementToBeClickable(element);
        element.click();
        element.clear();
        element.sendKeys(text);
        logger.info("Typed '" + text + "' into element " + element);
    }

    protected void selectElementByVisibleText(WebElement element, String text){
        waitForElementToBeClickable(element);
        Select select = new Select(element);
        select.selectByVisibleText(text);
        logger.info("Selected option '" + text + "' in element " + element);
    }

    protected void selectElementByPartialVisibleText(WebElement element, String partialText){
        Select select = new Select(element);
        WebElement desiredOption = select.getOptions().stream()
                        .filter(option -> option.getText().toLowerCase().contains(partialText.toLowerCase()))
                                .findFirst()
                                        .orElseThrow(() -> new IllegalArgumentException(
                                                "No option contains text: " + partialText
                                        ));
        select.selectByVisibleText(desiredOption.getText());
        logger.info("Selected option containing'" + partialText + desiredOption.getText() + "' in element " + element);
    }

    protected void waitForVisibility(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitForVisibility(WebElement element, int timeoutSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                    .until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            logger.warning("Timeout waiting for visibility of element: " + element);
            throw e;
        }
    }

    protected void waitForElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void switchToDefaultContent() {
         driver.switchTo().defaultContent();
    }

    protected void switchToFrame(WebElement iframe) {
        driver.switchTo().frame(iframe);
    }

    protected String getPageTitle() {
        return driver.getTitle();
    }
}
