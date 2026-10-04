package com.swaglabs.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportConstants {

	public static final String TIMESTAMP = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	public static final String REPORT_PATH = System.getProperty("extentReportPath",
			System.getProperty("user.dir") + "/reports/TestReport_" + TIMESTAMP + ".html");

}
