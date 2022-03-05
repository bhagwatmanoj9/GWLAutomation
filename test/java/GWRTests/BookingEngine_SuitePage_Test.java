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
import testHarness.GWRFunctions.BookingEngine_SuitePage_Function;
import testHarness.GWRFunctions.GWR_Confirmation_Page_Function;
import testHarness.GWRFunctions.GWR_HomePage_Function;

public class BookingEngine_SuitePage_Test extends SeleniumUtils {
	
	ReadExcelFile read = new ReadExcelFile();
	RecordVideo record = new RecordVideo();
	BookingEngine_SuitePage_Function suite = new BookingEngine_SuitePage_Function();
	Properties prop = new Properties();
	GWR_HomePage_Function home = new GWR_HomePage_Function();

	ReadingAndWritingTextFile emailread = new ReadingAndWritingTextFile();
	GWR_Confirmation_Page_Function confirmationPage = new GWR_Confirmation_Page_Function();
	BookingEngine_PaymentPage_Function payment = new BookingEngine_PaymentPage_Function();
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

			case "Suite Page:Verify that user should able to click on edit button":
				home.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
				reuse.searchThemSuite(scriptNo, dataSetNo, dataSheet);
				suite.verifyEditBtn(scriptNo, dataSetNo, dataSheet);


			case "Suite Page:Verify that user should able to see check-in check-out no of guest and offer code fields after click on edit":
				suite.verifyPlanFields(scriptNo, dataSetNo, dataSheet);
				
			case "Suite Page:Verify that user should able to see update your stay CTA after click on edit link":
				suite.verifyUpdateYorStayBtn(scriptNo, dataSetNo, dataSheet);
				
			case "Suite Page:Verify that user should able to click on Offer Details caret and should not able to see the information":
				suite.verifyOfferDetails(scriptNo, dataSetNo, dataSheet);
				
			case "Suite Page:Verify that user should able to see by default view suite details Text link is collapsed under room card":
				suite.verifySeeMoreWhenHide(scriptNo, dataSetNo, dataSheet);
				
			case "Suite Page:Verify that user should able to click on View More Details text link under room card and info of suite displayed":
				suite.verifySeeMoreDetails(scriptNo, dataSetNo, dataSheet);
				
			case "Suite Page:Verify that user should able to click on View More Details Text link and info of suite should be hide":
				suite.verifySeeMoreDetailsGetHideAfterClick(scriptNo, dataSetNo, dataSheet);
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
