package com.swaglabs.driver;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.swaglabs.utilities.EmailGeneration;
import com.swaglabs.utilities.ExtentReportManager;
import com.swaglabs.utilities.ReportConstants;

public class ListenerConfig extends TestBase implements ITestListener {
	
    public static int passedTests = 0;
    public static int failedTests = 0;
    public static int skippedTests = 0;
    
    public static List<String> passedTestNames = new ArrayList<>();
    public static List<String> failedTestNames = new ArrayList<>();
    public static List<String> skippedTestNames = new ArrayList<>();

	private ExtentReports extent = ExtentReportManager.getInstance();
	private ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

	@Override
	public void onTestStart(ITestResult result) {
		ExtentTest test = extent.createTest(result.getMethod().getMethodName());
		extentTest.set(test);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
        passedTests++;
        passedTestNames.add(result.getMethod().getMethodName());        
		extentTest.get().log(Status.PASS, "Test Passed");

		try {
			driver = (WebDriver) result.getTestClass().getRealClass().getField("driver").get(result.getInstance());

			if (driver != null) {
				String filePath = getScreenShot(result.getMethod().getMethodName(), driver);
				extentTest.get().addScreenCaptureFromPath(filePath, "Test Passed - Screenshot");
			}
		} catch (Exception e) {
			System.err.println("Error in onTestSuccess: " + e.getMessage());
		}
	}

	@Override
	public void onTestFailure(ITestResult result) {
        failedTests++;
        failedTestNames.add(result.getMethod().getMethodName());
		extentTest.get().log(Status.FAIL, "Test Failed");
		extentTest.get().fail(result.getThrowable());

		try {
			driver = (WebDriver) result.getTestClass().getRealClass().getField("driver").get(result.getInstance());

			if (driver != null) {
				String filePath = getScreenShot(result.getMethod().getMethodName(), driver);
				extentTest.get().addScreenCaptureFromPath(filePath, "Test Failed - Screenshot");
			}
		} catch (Exception e) {
			System.err.println("Error in onTestFailure: " + e.getMessage());
		}
	}
	
	@Override
	public void onTestSkipped(ITestResult result) {
	    skippedTests++;
	    skippedTestNames.add(result.getMethod().getMethodName());
	    extentTest.get().log(Status.SKIP, "Test Skipped");

	    try {
	        driver = (WebDriver) result.getTestClass().getRealClass().getField("driver").get(result.getInstance());

	        if (driver != null) {
	            String filePath = getScreenShot(result.getMethod().getMethodName(), driver);
	            extentTest.get().addScreenCaptureFromPath(filePath, "Test Skipped - Screenshot");
	        }
	    } catch (Exception e) {
	        System.err.println("Error in onTestSkipped: " + e.getMessage());
	    }
	}


	@Override
	public void onFinish(ITestContext context) {
		extent.flush();
		java.io.File reportFile = new java.io.File(ReportConstants.REPORT_PATH);
		if (!reportFile.isFile() || reportFile.length() == 0) {
			throw new IllegalStateException("Extent report was not generated: " + reportFile.getAbsolutePath());
		}
		System.out.println("Extent report generated: " + reportFile.getAbsolutePath());

		if (Boolean.parseBoolean(System.getProperty("sendReportEmail", "true"))) {
			try {
				EmailGeneration.sendReportEmail();
			} catch (IllegalStateException e) {
				System.err.println("Report email delivery failed; test results and Extent report are preserved.");
				e.printStackTrace(System.err);
			}
		}
	}
}
