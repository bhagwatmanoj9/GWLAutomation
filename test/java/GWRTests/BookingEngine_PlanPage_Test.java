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

public class BookingEngine_PlanPage_Test extends SeleniumUtils{  
	ReadExcelFile read = new ReadExcelFile();
	RecordVideo record = new RecordVideo();

	Properties prop = new Properties();

	ReadingAndWritingTextFile emailread = new ReadingAndWritingTextFile();
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

			case "Plan Page - Verify user should able to click on book CTA":
				plan.navigateToPlanThroughBBookNowBtn(scriptNo, dataSetNo, dataSheet); 

			case "Plan Page - Verify user should able to select Check-in, Checkout dates by clicking on SET DATES CTA on booking widget":
				plan.selectCheckInOutDates(scriptNo, dataSetNo, dataSheet); 
				
			case "Plan Page: Verify that by clicking on + button, user can increase the Adults and Kids quantity on Select Guests pop up in the Booking widget":
				plan.verifyGuestAbleToIncreaeCount(scriptNo, dataSetNo, dataSheet); 
				
			case "Plan Page: Verify that by clicking on minus button user can decrease the Adults and Kids quantity on Select Guests pop up in the Booking widget":
				plan.verifyGuestAbleToDecreCount(scriptNo, dataSetNo, dataSheet); 	
				
			case "Plan Page Verify that when user increases the Kids quantity then that no.of Quantity boxes should appear to update the age of the kid":
				plan.verifyKidDD(scriptNo, dataSetNo, dataSheet); 
				
			case "Plan Page: Verify that user should be able to set Kids age by selecting a number from the dropdown":
				plan.verifyKidAge(scriptNo, dataSetNo, dataSheet);  
				
			case "Plan Page: Verify that when user sets Guest count as a Max number then Notification msg should appear as The number of guests you have entered":
				plan.verifyAlertMsgForMaxGuest(scriptNo, dataSetNo, dataSheet);  
				
			case "Suite Page:Verify that user should able to click on edit button":
				//plan.verifyAlertMsgForMaxGuest(scriptNo, dataSetNo, dataSheet);
				
			case "Plan Page:Verify that user able to see Offer Code Error pop up when applied MOREFUN with 1 night stay":
				plan.verifyAlertMsgForMoreFun(scriptNo, dataSetNo, dataSheet);
				
					
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


}
