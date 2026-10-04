package com.swaglabs.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager {

	private static ExtentReports extent;

	public static ExtentReports getInstance() {
		if (extent == null) {
			extent = createInstance();
		}
		return extent;
	}

	private static ExtentReports createInstance() {

		String reportPath = ReportConstants.REPORT_PATH;
		try {
			Files.createDirectories(Path.of(reportPath).getParent());
		} catch (IOException e) {
			throw new IllegalStateException("Unable to create the Extent report directory", e);
		}

		ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
		sparkReporter.config().setReportName("SwagLabs Automation Test Report");
		sparkReporter.config().setDocumentTitle("Execution Report");
		sparkReporter.config().setTheme(Theme.DARK);
		sparkReporter.config().setEncoding("UTF-8");
		sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
		sparkReporter.config().setCss(".badge-primary { background-color: #28a745; }");

		// Main Extent object
		ExtentReports extent = new ExtentReports();
		extent.attachReporter(sparkReporter);

		// System Info (Optional but useful)
		extent.setSystemInfo("Project", "Swag Labs Automation");
		extent.setSystemInfo("Tester", "Aniket Umare");
		extent.setSystemInfo("OS", System.getProperty("os.name"));
		extent.setSystemInfo("Java Version", System.getProperty("java.version"));
		extent.setSystemInfo("Browser", "Chrome"); // You can dynamically update it too
		extent.setSystemInfo("Environment", "QA");

		// Set strategy to show per-test level reporting
		extent.setAnalysisStrategy(AnalysisStrategy.TEST);

		return extent;
	}
}
