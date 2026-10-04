package com.swaglabs.driver;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.swaglabs.helpers.TestData_Reader;
import com.swaglabs.pagelayer.UserLogin_Page;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestBase {

	public WebDriver driver; // WebDriver instance to control the browser
	public UserLogin_Page LogPage; // Instance of LoginPage for test interactions
	public TestData_Reader DataReader; // Configuration reader instance for reading test configurations

	// Constructor to initialize configReader
	public TestBase() {
		DataReader = new TestData_Reader();
	}

	// Method to initialize WebDriver based on browser configuration
	public WebDriver initializeDriver() {

		// Read browser name from system property or config file
		String browserName = System.getProperty("browser") != null ? System.getProperty("browser")
				: DataReader.getProperty("browser");

		if (browserName.contains("chrome")) {
			ChromeOptions opt = new ChromeOptions();
			WebDriverManager.chromedriver().setup();
			opt.addArguments("start-maximized"); // Maximize browser window
			opt.setAcceptInsecureCerts(true); // Accept insecure certificates
			opt.addArguments("incognito"); // Enable incognito mode

			// Check if headless mode is enabled
			if (browserName.contains("headless")) {
				opt.addArguments("--headless=new"); // Run Chrome in headless mode (no GUI)
			}

			driver = new ChromeDriver(opt); // Initialize ChromeDriver with options

		} else if (browserName.equalsIgnoreCase("firefox")) {
			// FirefoxOptions opt = new FirefoxOptions();
			// opt.addArguments("start-maximized");
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver(); // Initialize FirefoxDriver

		} else if (browserName.equalsIgnoreCase("edge")) {
			EdgeOptions opt = new EdgeOptions();
			opt.addArguments("start-maximized"); // Maximize browser window
			opt.setAcceptInsecureCerts(true); // Accept insecure certificates
			opt.addArguments("incognito"); // Enable incognito mode
			WebDriverManager.edgedriver().setup();

			driver = new EdgeDriver(opt); // Initialize EdgeDriver with options
		}

		int implicitWait = Integer.parseInt(DataReader.getProperty("implicitWait"));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait)); // Set implicit wait
		return driver;
	}

	// Method executed before each test method to open the application
	@BeforeMethod(alwaysRun = true)
	public UserLogin_Page OpenSwaglabs() {
		DataReader = new TestData_Reader(); // Initialize configReader
		driver = initializeDriver(); // Initialize WebDriver
		driver.get(DataReader.getProperty("baseURL")); // Open the application URL

		LogPage = new UserLogin_Page(driver); // Initialize LoginPage with WebDriver instance
		return LogPage;
	}

	// Method executed after each test method to tear down and quit the WebDriver
	@AfterMethod
	public void tearDown() throws InterruptedException {
		Thread.sleep(5000); // Wait for 5 seconds before quitting the browser
		if (driver != null) {
			driver.quit(); // Quit the WebDriver
		}
	}

	// Method to capture screenshot and save it
	public String getScreenShot(String TestCaseName, WebDriver driver) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver; // Cast WebDriver to TakesScreenshot
		File src = ts.getScreenshotAs(OutputType.FILE); // Capture screenshot as FILE
		File dest = new File(System.getProperty("user.dir") + "//reports//" + TestCaseName + ".png");
		FileUtils.copyFile(src, dest); // Copy screenshot to destination
		return System.getProperty("user.dir") + "//reports//" + TestCaseName + ".png"; // Return screenshot path
	}

}
