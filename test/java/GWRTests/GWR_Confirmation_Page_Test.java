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
import testHarness.GWRFunctions.GWR_Confirmation_Page_Function;

public class GWR_Confirmation_Page_Test extends SeleniumUtils { 

	ReadExcelFile read = new ReadExcelFile();
	RecordVideo record = new RecordVideo();

	Properties prop = new Properties();

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

			case "E2E: Verify that user should able to see correct adults and kid count under Adults section when user book standard suite":
				confirmationPage.navigateToConfirmationAsGuestWithStandSuite(scriptNo, dataSetNo, dataSheet); 
				confirmationPage.verifyGuestCountOnCmp();

			case "E2E: Verify that user should able to see correct adults and kid count under Adults section when user book themed suite":
				// confirmationPage.verifyGuestCountOnConfirmation(scriptNo, dataSetNo,
				// dataSheet);
				confirmationPage.navigateToConfirmationAsGuestWithThemSuite(scriptNo, dataSetNo, dataSheet);
				payment.verifyGuestCountOnCmp();

			case "E2E: Verify that user should able to see correct adults and kid count under Adults section when user book premium suite":
				confirmationPage.navigateToConfirmationAsGuestWithPremiumSuite(scriptNo, dataSetNo, dataSheet);
				reuse.verifyGuestCountOnCmp();

			case "E2E: Verify that user should able to see correct check-in, check-out dates under stay dates section as well in booking mail when user book standard suite":
				confirmationPage.verifyCheckInOutOnCmpforStandardSuite(scriptNo, dataSetNo, dataSheet);
				reuse.verifyCheckInOutOnCmp();

			case "E2E: Verify that user should able to see correct check-in, check-out dates under stay dates section as well in booking mail when user book themed suite till the email":
				confirmationPage.verifyCheckInOutaOnCmpforThemeSuite(scriptNo, dataSetNo, dataSheet);
				reuse.verifyCheckInOutOnCmp();

			case "E2E: Verify that user should able to see correct check-in, check-out dates under stay dates section as well in booking mail when user book premium suite till the email":
				confirmationPage.verifyheckInOutaOnCmpforPremiumSuite(scriptNo, dataSetNo, dataSheet);
				reuse.verifyCheckInOutOnCmp();

			case "E2E: Verify that user should able to book successful reservation when select suite type as standard":
				confirmationPage.bookReserAsGuestWithStandSuite(scriptNo, dataSetNo, dataSheet);
				confirmationPage.verifySelectedsuite();

			case "E2E: Verify that user should able to book successful reservation when select suite type as Themed":
				confirmationPage.bookReserAsGuestWithThemSuite(scriptNo, dataSetNo, dataSheet);
				confirmationPage.verifySelectedsuite();

			case "E2E: Logged User Verify that user should able to make successful reservation with accessible suite having guest [Adult, 1billable, 1nonbillable] with cred card as payment method and cost summary reflections should be correct till email":
				confirmationPage.verifyTotalBtnPayAndCmpForStand(scriptNo, dataSetNo, dataSheet);
				//confirmationPage.verifyTotalOnCmp(); 
				
			case "E2E: Logged User Verify that user should able to make successful reservation with accessible suite having guest only adult with cred card as payment method and cost ":
				confirmationPage.bookAccessibleSuitewithGuestUsrWithAdultOnly(scriptNo, dataSetNo, dataSheet);
			
			
		case "E2E: Guest User Verify that user should able to make successful reservation with accessible suite having guest only adult with cred card as payment method and cost summary ":
			confirmationPage.bookAccessibleSuitewithLogUsrWithAdultOnly(scriptNo, dataSetNo, dataSheet);
			
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
