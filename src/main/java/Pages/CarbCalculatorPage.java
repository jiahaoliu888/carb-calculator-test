package Pages;

import Common.BasePage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class CarbCalculatorPage extends BasePage {

    // Relative URL path for the carbohydrate calculator page on the website
    private String CarbCalPath = "/carbohydrate-calculator.html";

    // =============== Common Elements ===============

    @FindBy(xpath = "//h1[text()='Carbohydrate Calculator']")
    private WebElement CalCalculatorHdr;

    @FindBy(id = "cage")
    private WebElement ageTxt;

    @FindBy(css = "label[for='csex1']")
    private WebElement genderMale;

    @FindBy(css = "label[for='csex2']")
    private WebElement genderFemale;

    @FindBy(css = "select[name=\"cactivity\"]")
    private WebElement activityDrpDwn;

    @FindBy(xpath = "//a[contains(text(), \"Settings\")]")
    private WebElement settingsLnk;

    @FindBy(xpath = "//input[@name='cformula']/parent::label")
    private List<WebElement> formulaRdo;

    @FindBy(name = "cfatpct")
    private WebElement bodyFatTxt;

    @FindBy(name = "x")
    private WebElement submitBtn;

    @FindBy(css = "input[value=\"Clear\"]")
    private WebElement clearBtn;

    @FindBy(xpath = "//h2[text()='Result']")
    private WebElement resultImg;

    @FindBy(className = "cinfoT")
    private WebElement resultTbl;

    @FindBy(xpath = "//td[contains(text(), 'week')]")
    private List<WebElement> goalList;

    @FindBy(xpath = "//td[contains(text(), 'Calories')]")
    private List<WebElement> dailyCaloriesList;

    @FindBy(xpath = "//td/b[contains(text(), 'grams')]")
    private List<WebElement> carbsGramsList;

    @FindBy(css = "\"table.cinfoT td\"")
    private List<WebElement> cells;

    @FindBy(id = "cageifcErr")
    private WebElement ageInputErrorMsg;

    @FindBy(id = "cheightmeterifcErr")
    private WebElement heightInputErrorMsg;

    @FindBy(id = "ckgifcErr")
    private WebElement kgInputErrorMsg;

    @FindBy(id = "cfatpctifcErr")
    private WebElement bodyFatInputErrorMsg;

    @FindBy(xpath = "//div/font[text()='Please provide an age between 18 and 80.']")
    private WebElement ageBoundaryWarningMsg;

    /* ===============  Metric units =============== */
    @FindBy(id = "menuon")
    private WebElement metricUnitsTab;

    @FindBy(id = "cheightmeter")
    private WebElement heightMeterTxt;

    @FindBy(id = "ckg")
    private WebElement weightKgTxt;

    /* ===============  US units =============== */
    @FindBy(xpath = "//a[text()='US Units']")
    private WebElement USUnitsTab;

    @FindBy(id = "cheightfeet")
    private WebElement heightFeetTxt;

    @FindBy(id = "cheightinch")
    private WebElement heightInchTxt;

    @FindBy(id = "cpound")
    private WebElement weightPoundTxt;

    /* ===============  Other units =============== */
    @FindBy(xpath = "//a[text()='Other Units']")
    private WebElement otherUnitsTab;

    @FindBy(xpath = "//iframe[contains(@src, 'converter')]")
    private WebElement ucIframe;

    @FindBy(xpath = "//div[contains(., 'Use this converter to convert to the unit accepted')]")
    private WebElement unitConverterHdr;

    @FindBy(xpath = "//li/a[contains(text(), 'Length')]")
    private WebElement lengthTab;

    @FindBy(xpath = "//li/a[text()='Temperature']")
    private WebElement temperatureTab;

    @FindBy(xpath = "//li/a[text()='Area']")
    private WebElement areaTab;

    @FindBy(xpath = "//li/a[text()='Volume']")
    private WebElement volumeTab;

    @FindBy(xpath = "//li/a[text()='Weight']")
    private WebElement weightTab;

    @FindBy(name = "fromVal")
    private WebElement fromTxt;

    @FindBy(name = "toVal")
    private WebElement toTxt;

    @FindBy(id = "calFrom")
    private WebElement fromList;

    @FindBy(id = "calTo")
    private WebElement toList;


    public CarbCalculatorPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToCarbCalculatorPage(String baseURL) {
        driver.navigate().to(baseURL + CarbCalPath);
        logger.info("Navigate to Carb Calculator page: " + baseURL + CarbCalPath);
    }

    public void waitForPageLoaded() {
        waitForVisibility(CalCalculatorHdr, 5);
        logger.info("######## page title = \" + getPageTitle() + \"########");
    }

    public void enterAge(String age) {
        type(ageTxt, age);
    }

    public void selectGender(String gender) {
        if(gender.toLowerCase().startsWith("m")) {
            genderMale.click();
        } else if(gender.toLowerCase().startsWith("f")) {
            genderFemale.click();
        } else {
            throw new IllegalArgumentException("Unsupport gender: " + gender);
        }
        logger.info("Selected gender option: " + gender);
    }

    public void selectActivityOption(String option) {
        selectElementByPartialVisibleText(activityDrpDwn, option);
    }

    public void submitCalculation() {
        submitBtn.click();
    }

    public boolean waitUntilResultIsVisible() {
        try {
            waitForVisibility(resultImg, 2);
            return true;
        } catch(TimeoutException e) {
            return false;
        }
    }

    public void expandSettings() {
        if(settingsLnk.getText().contains("+"))
           settingsLnk.click();
    }


    public void collapseSettings() {
        if(settingsLnk.getText().contains("-"))
            settingsLnk.click();
    }

    public void selectBMRFormula(String formula) {
        formulaRdo.stream()
                .filter(element -> element.getText().trim().equalsIgnoreCase(formula.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("BMR formula not found: " + formula))
                .click();
    }

    public void enterBodyFat(String bf) {
        type(bodyFatTxt, bf);
    }

    public boolean isAgeErrorMsgDisplayed() {
        return ageInputErrorMsg.isDisplayed();
    }

    public boolean isHeightErrorMsgDisplayed() {
        return heightInputErrorMsg.isDisplayed();
    }

    public boolean isKgErrorMsgDisplayed() {
        return kgInputErrorMsg.isDisplayed();
    }

    public boolean isBodyFatErrorMsgDisplayed() {
        return bodyFatInputErrorMsg.isDisplayed();
    }

    public boolean isAgeErrorMsgCorrect() {
        return ageInputErrorMsg.getText().contains("positive numbers only");
    }

    public boolean isHeightErrorMsgCorrect() {
        return heightInputErrorMsg.getText().contains("positive numbers only");
    }

    public boolean isKgErrorMsgCorrect() {
        return kgInputErrorMsg.getText().contains("positive numbers only");
    }

    public boolean isBodyFatErrorMsgCorrect() {
        return bodyFatInputErrorMsg.getText().contains("positive numbers only");
    }

    public boolean isAgeBoundaryWarningMsgDisplayed() {
        return ageBoundaryWarningMsg.isDisplayed();
    }

    public List<String> getElementsTextList(List<WebElement> elements) {
        waitForVisibility(resultTbl);
        return elements.stream()
                .map(WebElement::getText)
                .map(String::trim)
                .toList();
    }

    public List<String> getDailyCaloriesStringList() {
        return getElementsTextList(dailyCaloriesList);
    }

    public List<String> getCarbsGramsStringList() {
        return getElementsTextList(carbsGramsList);
    }

    /**************************************************************
     **                       Metric Units                       **
     **************************************************************/
    public void switchToMetricUnitsTab() {
        metricUnitsTab.click();
        waitForVisibility(unitConverterHdr);
    }

    public void enterHeightMeter(String height) {
        type(heightMeterTxt, height);
    }

    public void enterWeightKg(String weight) {
        type(weightKgTxt, weight);
    }

    public boolean isMetricUnitResult(){
        try {
            return goalList.stream()
                    .allMatch(element -> element.getText().toLowerCase().contains("kg"));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**************************************************************
     **                        US Units                          **
     **************************************************************/
    public void switchToUSUnitsTab() {
        USUnitsTab.click();
    }

    public void enterHeightFeet(String height) {
        type(heightFeetTxt, height);
    }

    public void enterHeightInch(String height) {
        type(heightInchTxt, height);
    }

    public void enterWeightPound(String weight) {
        type(weightPoundTxt, weight);
    }

    public boolean isUSUnitResult(){
        try {
            return goalList.stream()
                    .allMatch(element -> element.getText().toLowerCase().contains("lb"));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**************************************************************
     **                      Other Units                          **
     **************************************************************/

    public void switchToOtherUnitsTab() {
        otherUnitsTab.click();
    }

    public boolean isUnitConverterHeaderDisplayed() {
        waitForVisibility(unitConverterHdr);
        return unitConverterHdr.isDisplayed();
    }

    public void switchToLengthSubTab() {
        lengthTab.click();
    }

    public void switchToTemperatureSubTab() {
        temperatureTab.click();
    }

    public void switchToAreaSubTab() {
        areaTab.click();
    }

    public void switchToVolumeSubTab() {
        volumeTab.click();
    }

    public void switchToWeightSubTab() {
        weightTab.click();
    }

    public void enterFromVal(String fromVl) {
        type(fromTxt, fromVl);
    }

    public String getToVal() {
        waitForVisibility(toTxt);
        return toTxt.getAttribute("value");
    }

    public void selectFromList(String option) {
        fromTxt.clear(); // Clear the input field to avoid unintended changes in the To: dropdown options
        fromTxt.sendKeys(Keys.ENTER);
        selectElementByVisibleText(fromList, option);
    }

    public void selectToList(String option) {
        selectElementByVisibleText(toList, option);
    }

    public void switchToUnitConverterFrame() {
        switchToFrame(ucIframe);
    }

}
