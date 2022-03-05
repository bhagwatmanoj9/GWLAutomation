package GWRTests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import baseClasses.ExtentTestManager;
import baseClasses.ReadExcelFile;
import baseClasses.ReadingAndWritingTextFile;
import baseClasses.RecordVideo;
import baseClasses.Report;
import baseClasses.ReusableUtils;
import baseClasses.SeleniumUtils;
import testHarness.GWRFunctions.BookingEngine_PaymentPage_Function;
import testHarness.GWRFunctions.BookingEngine_PlanPage_Function;
import testHarness.GWRFunctions.GWR_Confirmation_Page_Function;
import testHarness.GWRFunctions.GWR_HomePage_Function;

public class GWR_HomePage_Test extends SeleniumUtils{ 
	
	ReadExcelFile read = new ReadExcelFile();
	RecordVideo record = new RecordVideo();

	Properties prop = new Properties();

	ReadingAndWritingTextFile emailread = new ReadingAndWritingTextFile();
	GWR_HomePage_Function homePage = new GWR_HomePage_Function(); 
	BookingEngine_PlanPage_Function plan = new BookingEngine_PlanPage_Function();
	ReusableUtils reuse = new ReusableUtils();

	@Parameters({ "scriptNo", "dataSetNo", "dataSheet", "ScenarioName" })
	@Test
	public void launchScreen(String scriptNo, String dataSetNo, String dataSheet, String ScenarioName)
			throws Exception {
		Report.createTestCaseReportHeader(ScenarioName);
		String path1 = new File(".").getCanonicalPath();
		try (InputStream input = new FileInputStream(path1 + "/configuration.properties")) {
			// load a properties file
			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		String botId = prop.getProperty("BotId");
		record.startVideoRecording(botId, ScenarioName); 
		try {
			switch (ScenarioName) {
			
		case "Mobile- Home Page: Verify that user navigate on plan page when click on 'Book Now' button of Room Card":
			homePage.navigateToPlanThroughRoomCardBtn(scriptNo, dataSetNo, dataSheet);
			
		case "Special Offers: Verify that user able to see alert message for ESAVER when select next dates within 60 days from today's date and search suites through 'Apply Code' button on Deal pages":
			homePage.verifyAlertMsgForEASER(scriptNo, dataSetNo, dataSheet);
			
	
			//Special Offers: Verify that user able to see alert message for ESAVER when select next dates within 60 days from today's date and search suites through 'Apply Code' button on Deal page
		}

		} catch (Exception e) {
			Report.LogInfo("Exception", "Exception in " + ScenarioName + ": " + e.getMessage(), "FAIL");
			ExtentTestManager.getTest().log(LogStatus.ERROR, "Exception in " + ScenarioName + ": " + e.getMessage());
			ExtentTestManager.getTest().log(LogStatus.INFO,
					ExtentTestManager.getTest().addBase64ScreenShot(Capturefullscreenshot()));
		}
		Report.createTestCaseReportFooter();
		Report.SummaryReportlog(ScenarioName);
		record.stopVideoRecording();
	}
	public void manojtest(String scriptNo, String dataSetNo, String dataSheet, String ScenarioName) 
	{
		
		QA
	}

}
