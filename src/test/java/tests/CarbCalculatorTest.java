package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class CarbCalculatorTest extends BaseTest {

    //Path to the dataset file containing test input data
    private static final String testInputFile = "src/main/resources/dataset/CarbCalculatorTestData.txt";

    //Map to store test data as key-value pairs
    private HashMap<String, String> dataMap = null;

    @BeforeClass
    public void setUp() {
        loadTestProperties();
        loadTestCaseInput(testInputFile);
    }

    @BeforeMethod
    public void init(Method method) {
        initializeUITest();
        String tcName = method.getName();
        logger.info("read test data for test case " + tcName);
        dataMap = getTestdata(tcName);
        if (dataMap != null) {
            dataMap.forEach((key, value) -> logger.info(key + ":" + value));
        }
        logger.info("================ UI test case: " + tcName + " started ================ ");
        carbCalPage.navigateToCarbCalculatorPage(getBaseURL());
    }

    @Test(
            enabled = true,
            description = "TC023 Verify that the Carbohydrates Calculator works correctly with valid input using Metric Units."
    )
    public void verifyCarbCalculatorWithValidInputMetricUnit() {

        // Enter valid Metric Units input
        carbCalPage.enterAge(dataMap.get("age"));
        carbCalPage.selectGender(dataMap.get("gender"));
        carbCalPage.enterHeightMeter(dataMap.get("cm"));
        carbCalPage.enterWeightKg(dataMap.get("kg"));
        carbCalPage.selectActivityOption(dataMap.get("activity"));

        // Submit and verify result is returned and in Metric Units
        carbCalPage.submitCalculation();
        Assert.assertTrue(
                carbCalPage.waitUntilResultIsVisible(),
                "Result should be visible after submitting calculation."
        );

        Assert.assertTrue(
                carbCalPage.isMetricUnitResult(),
                "Result should be displayed in Metric Units."
        );

        // Verify results are non-empty and log output
        List<String> dailyCalories = carbCalPage.getDailyCaloriesStringList();
        List<String> carbsGrams = carbCalPage.getCarbsGramsStringList();
        verifyResultsNotEmptyAndLog(dailyCalories,carbsGrams);
    }

    @Test(
            enabled = true,
            description = "TC024 Verify that the Carbohydrates Calculator works correctly with valid input using US Units."
    )
    public void verifyCarbCalculatorWithValidInputUSUnit() {

        // Switch to US Units and enter required inputs
        carbCalPage.switchToUSUnitsTab();
        carbCalPage.enterAge(dataMap.get("age"));
        carbCalPage.selectGender(dataMap.get("gender"));
        carbCalPage.enterHeightFeet(dataMap.get("feet"));
        carbCalPage.enterHeightInch(dataMap.get("inch"));
        carbCalPage.enterWeightPound(dataMap.get("pound"));
        carbCalPage.selectActivityOption(dataMap.get("activity"));

        // Submit and verify result is returned and in US Units
        carbCalPage.submitCalculation();
        Assert.assertTrue(
                carbCalPage.waitUntilResultIsVisible(),
                "Result should be visible after submitting calculation."
        );
        Assert.assertTrue(
                carbCalPage.isUSUnitResult(),
                "Result should be displayed in US Units."
        );

        // Verify results are non-empty and log output
        List<String> dailyCalories = carbCalPage.getDailyCaloriesStringList();
        List<String> carbsGrams = carbCalPage.getCarbsGramsStringList();
        verifyResultsNotEmptyAndLog(dailyCalories,carbsGrams);
    }

    @Test(
            enabled = true,
            description = "TC025 Verify that the Carbohydrates Calculator works with different BMR formula and body fat percentage."
    )
    public void verifyCarbCalculatorWithDiffFormulaAndBodyFat() {
        // Enter required inputs
        carbCalPage.enterAge(dataMap.get("age"));
        carbCalPage.selectGender(dataMap.get("gender"));
        carbCalPage.enterHeightMeter(dataMap.get("cm"));
        carbCalPage.enterWeightKg(dataMap.get("kg"));
        carbCalPage.selectActivityOption(dataMap.get("activity"));

        // Use advanced settings: select BMR formula and enter body fat
        carbCalPage.expandSettings();
        carbCalPage.selectBMRFormula(dataMap.get("formula"));
        carbCalPage.enterBodyFat(dataMap.get("bodyFat"));

        // Submit and verify result is returned and in US Units
        carbCalPage.submitCalculation();
        Assert.assertTrue(
                carbCalPage.waitUntilResultIsVisible(),
                "Result should be visible after submitting calculation."
        );

        Assert.assertTrue(
                carbCalPage.isMetricUnitResult(),
                "Result should be displayed in Metric Units."
        );
        // Verify results are non-empty and log output
        List<String> dailyCalories = carbCalPage.getDailyCaloriesStringList();
        List<String> carbsGrams = carbCalPage.getCarbsGramsStringList();
        verifyResultsNotEmptyAndLog(dailyCalories,carbsGrams);
    }

    @Test(
            enabled = true,
            description = "TC026 Verify that the Carbohydrates Calculator correctly handles invalid inputs."
    )
    public void verifyCarbCalculatorWithInvalidInput() {

        // Negative age
        carbCalPage.enterAge("-1");
        Assert.assertTrue(
                carbCalPage.isAgeErrorMsgDisplayed() && carbCalPage.isAgeErrorMsgCorrect(),
                "Expected age error message is missing or incorrect."
        );
        carbCalPage.enterAge(dataMap.get("age"));

        // Non-numeric height
        carbCalPage.enterHeightMeter("ab");
        Assert.assertTrue(
                carbCalPage.isHeightErrorMsgDisplayed() && carbCalPage.isHeightErrorMsgCorrect(),
                "Expected height error message is missing or incorrect."
        );
        carbCalPage.enterHeightMeter(dataMap.get("cm"));

        // Non-numeric weight
        carbCalPage.enterWeightKg("xyz");
        Assert.assertTrue(
                carbCalPage.isKgErrorMsgDisplayed() && carbCalPage.isKgErrorMsgCorrect(),
                "Expected weight error message is missing or incorrect."
        );
        carbCalPage.enterWeightKg(dataMap.get("kg"));

        // Expand advanced settings
        carbCalPage.selectActivityOption(dataMap.get("activity"));
        carbCalPage.expandSettings();
        carbCalPage.selectBMRFormula(dataMap.get("formula"));

        // Invalid body fat input
        carbCalPage.enterBodyFat("#");
        Assert.assertTrue(
                carbCalPage.isBodyFatErrorMsgDisplayed() && carbCalPage.isBodyFatErrorMsgCorrect(),
                "Expected body fat error message is missing or incorrect."
        );
        carbCalPage.enterBodyFat(dataMap.get("bodyFat"));

        carbCalPage.collapseSettings();

        // Submit final calculation with valid data, check boundary warning
        carbCalPage.submitCalculation();
        Assert.assertTrue(
                carbCalPage.isAgeBoundaryWarningMsgDisplayed(),
                "Age boundary warning message should be displayed."
        );
    }

    @Test(
            enabled = true,
            description = "TC027 Verify that the Carbohydrates Calculator outputs consistent results when using Metric and US units."
    )
    public void verifyCarbCalculatorConsistentResultsAcrossUnits() {

        // Submit calculation in Metric Units
        carbCalPage.enterAge(dataMap.get("age"));
        carbCalPage.selectGender(dataMap.get("gender"));
        carbCalPage.enterHeightMeter(dataMap.get("cm"));
        carbCalPage.enterWeightKg(dataMap.get("kg"));
        carbCalPage.selectActivityOption(dataMap.get("activity"));
        carbCalPage.submitCalculation();

        // Verify results are non-empty and log output
        List<String> metricDailyCaloriesList = carbCalPage.getDailyCaloriesStringList();
        List<String> metricCarbsGramsList = carbCalPage.getCarbsGramsStringList();
        verifyResultsNotEmptyAndLog(metricDailyCaloriesList,metricCarbsGramsList);

        // Submit calculation in US Units
        carbCalPage.switchToUSUnitsTab();
        carbCalPage.enterAge(dataMap.get("age"));
        carbCalPage.selectGender(dataMap.get("gender"));
        carbCalPage.enterHeightFeet(dataMap.get("feet"));
        carbCalPage.enterHeightInch(dataMap.get("inch"));
        carbCalPage.enterWeightPound(dataMap.get("pound"));
        carbCalPage.selectActivityOption(dataMap.get("activity"));
        carbCalPage.submitCalculation();

        // Verify results are non-empty and log output
        List<String> usDailyCaloriesList = carbCalPage.getDailyCaloriesStringList();
        List<String> usCarbsGramsList = carbCalPage.getCarbsGramsStringList();
        verifyResultsNotEmptyAndLog(usDailyCaloriesList,usCarbsGramsList);

        // Validate results are consistent
        Assert.assertEquals(
                metricDailyCaloriesList,
                usDailyCaloriesList,
                "Daily calories list mismatch between Metric and US Units."
        );

        Assert.assertEquals(
                metricCarbsGramsList,
                usCarbsGramsList,
                "Carbs grams list mismatch between Metric and US Units."
        );
    }

    @Test(
            enabled = true,
            description = "TC028 Verify unit conversions are accurate in the carbohydrate calculator."
    )
    public void verifyCarbCalculatorUnitConversionAccuracy() {

        carbCalPage.switchToOtherUnitsTab();
        carbCalPage.switchToUnitConverterFrame();
        Assert.assertTrue(carbCalPage.isUnitConverterHeaderDisplayed(), "Unit Converter is not displayed.");

        double tolerance = Double.parseDouble(dataMap.get("tolerance"));

        // Length
        verifyUnitConversion(
                carbCalPage::switchToLengthSubTab,
                "Inch",
                "Centimeter",
                dataMap.get("inch"),
                dataMap.get("cm"),
                tolerance
        );

        // Temperature
        verifyUnitConversion(
                carbCalPage::switchToTemperatureSubTab,
                "Celsius",
                "Fahrenheit",
                dataMap.get("CDegree"),
                dataMap.get("fDegree"),
                tolerance
        );

        // Area
        verifyUnitConversion(
                carbCalPage::switchToAreaSubTab,
                "Square Foot",
                "Square Meter",
                dataMap.get("sFoot"),
                dataMap.get("sMeter"),
                tolerance
        );
        // Volume
        verifyUnitConversion(
                carbCalPage::switchToVolumeSubTab,
                "US Gallon",
                "Liter",
                dataMap.get("gallon"),
                dataMap.get("liter"),
                tolerance
        );

        // Weight
        verifyUnitConversion(
                carbCalPage::switchToWeightSubTab,
                "Pound",
                "Kilogram ",
                dataMap.get("pound"),
                dataMap.get("kg"),
                tolerance
        );
    }

    private void verifyResultsNotEmptyAndLog(List<String> dailyCalories, List<String> carbsGrams) {
        System.out.println("Daily Calories Allowance: " + dailyCalories);
        System.out.println("Carbohydrates Grams: " + carbsGrams);

        Assert.assertFalse(dailyCalories.isEmpty(), "Daily calories result should not be empty.");
        Assert.assertFalse(carbsGrams.isEmpty(), "Carbohydrates grams result should not be empty.");
    }

    private void verifyUnitConversion(
            Runnable switchToSubTab,
            String fromUnit,
            String toUnit,
            String inputValue,
            String expectedValue,
            double tolerance
    ) {
        switchToSubTab.run();
        carbCalPage.selectFromList(fromUnit);
        carbCalPage.selectToList(toUnit);
        carbCalPage.enterFromVal(inputValue);

        double actual = Double.parseDouble(carbCalPage.getToVal());
        double expected = Double.parseDouble(expectedValue);

        Assert.assertEquals(actual, expected, tolerance,
                String.format("Conversion from %s to %s is incorrect. Expected: %s, Actual: %s",
                        fromUnit, toUnit, expected, actual)
        );
    }

    @AfterMethod
    public void after() {
        logger.info("Quitting WebDriver after each test case to ensure clean state.");
        cleanUp();
    }

    @AfterClass
    public void tearDown() {
        logger.info("All tests completed for CarbCalculatorTest. WebDriver quit handled after each test.");
    }
}
