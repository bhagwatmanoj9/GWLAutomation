package testHarness.GWRFunctions;

import java.io.File;
import java.io.IOException;

import baseClasses.Configuration;
import baseClasses.DataMiner;
import baseClasses.ReadExcelFile;
import baseClasses.ReusableUtils;
import baseClasses.SeleniumUtils;
import pageObjects.GWRObjects.GWR_Confirmation_Page_Obj;
import pageObjects.GWRObjects.GWR_HomePage_Obj;

public class GWR_HomePage_Function extends SeleniumUtils {

	ReusableUtils reuse = new ReusableUtils();
	ReadExcelFile read = new ReadExcelFile();
	//GWR_HomePage_Function homePage = new GWR_HomePage_Function();

	String dataFileName = "GWR_testdata.xlsx";
	File path = new File("./TestData/" + dataFileName);
	String testDataFile = path.toString();

	public void navigateToHomePage(String scriptNo, String dataSetNo, String dataSheet) throws Exception {
		String ProURL = DataMiner.fngetcolvalue(testDataFile, "Config_Sheet", scriptNo, "1", "UrlNavigate");
		openurl(Configuration.GWR_URL + "/" + ProURL);
		Thread.sleep(5000);
		reuse.closeCookies();
		Thread.sleep(30000);
		reuse.closeLeadGenpopUp();

	}

	public void loginIntoGWR(String scriptNo, String dataSetNo, String dataSheet)
			throws InterruptedException, IOException {
		String email = DataMiner.fngetcolvalue(testDataFile, dataSheet, scriptNo, dataSetNo, "Email");
		String password = DataMiner.fngetcolvalue(testDataFile, dataSheet, scriptNo, dataSetNo, "Password");

		verifyExists(GWR_HomePage_Obj.homePage.UserMenue, "icon of menue");
		click(GWR_HomePage_Obj.homePage.UserMenue, "icon of menue");

		verifyExists(GWR_HomePage_Obj.homePage.signInButton, "Sign In Button");
		click(GWR_HomePage_Obj.homePage.signInButton, "Sign In Button");
		verifyExists(GWR_HomePage_Obj.homePage.emailid, "emailid textbox");
		System.out.println(email);
		sendKeys(GWR_HomePage_Obj.homePage.emailid, email, "Login name");

		verifyExists(GWR_HomePage_Obj.homePage.pass, "pass textbox");
		sendKeys(GWR_HomePage_Obj.homePage.pass, password, "pass feld");

		System.out.println(password);
		Thread.sleep(2000);
		verifyExists(GWR_HomePage_Obj.homePage.login_button, "verify login GWR application");
		click(GWR_HomePage_Obj.homePage.login_button, "verify login GWR application");
		Thread.sleep(5000);

		verifyExists(GWR_HomePage_Obj.homePage.UserMenue, "icon of menue");
		click(GWR_HomePage_Obj.homePage.UserMenue, "icon of menue");
		verifyExists(GWR_HomePage_Obj.homePage.my_account, "my_account tab");
		System.out.println("Successfully logged into applicaton");
		click(GWR_HomePage_Obj.homePage.closeIcon, "close");

	}

	public void navigateToPlanThroughRoomCardBtn(String scriptNo, String dataSetNo, String dataSheet) throws Exception {
		Thread.sleep(2000);
		reuse.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		verifyExists(GWR_HomePage_Obj.homePage.bookonroomcard, "book CTA");
		click(GWR_HomePage_Obj.homePage.bookonroomcard, "book CTA");
		Thread.sleep(2000);
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.CheckInDate, "select date field dates");
		Thread.sleep(2000); 

	}

	public void verifyAlertMsgForEASER(String scriptNo, String dataSetNo, String dataSheet) throws Exception {
		
		String Offer = DataMiner.fngetcolvalue(testDataFile, dataSheet, scriptNo, dataSetNo, "Offer"); 

		Thread.sleep(2000);
		reuse.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		loginIntoGWR(scriptNo, dataSetNo, dataSheet);
		verifyExists(GWR_HomePage_Obj.homePage.UserMenue, "icon of menue");
		click(GWR_HomePage_Obj.homePage.UserMenue, "icon of menue");
		verifyExists(GWR_HomePage_Obj.homePage.deals, "Deals menue");
		click(GWR_HomePage_Obj.homePage.deals, "Deals menue");
		verifyExists(GWR_HomePage_Obj.homePage.deals2, "Deals sub menue");
		click(GWR_HomePage_Obj.homePage.deals2, "Deals sub menue");
		verifyExists(GWR_HomePage_Obj.homePage.Apply, "apply code CTA"); 
		click(GWR_HomePage_Obj.homePage.Apply, "apply code CTA");
		Thread.sleep(3000); 
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest"); 
		click(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest"); 

		reuse.selectAdults();

		click(GWR_Confirmation_Page_Obj.confirmationObj.DoneBtn, "click Done btn");

		

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.CheckInDate, "select date field dates");
		click(GWR_Confirmation_Page_Obj.confirmationObj.CheckInDate, "select date field dates");

		// sendDates(GWR_Confirmation_Page_Obj.confirmationObj.CheckInDate);

		verifyExists(GWR_HomePage_Obj.homePage.checkin_Esaver, "select check in dates");
		click(GWR_HomePage_Obj.homePage.checkin_Esaver, "select check in dates");

		verifyExists(GWR_HomePage_Obj.homePage.checkout_Esaver, "select check out dates"); 
		click(GWR_HomePage_Obj.homePage.checkout_Esaver, "select check out dates");

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.SetBtn, "CLick on sublit btn");
		click(GWR_Confirmation_Page_Obj.confirmationObj.SetBtn, "CLick on sublit btn");
		Thread.sleep(5000);

		click(GWR_Confirmation_Page_Obj.confirmationObj.SubmitBtn, "booknow submit button click");
		Thread.sleep(5000);
		
		verifyExists(GWR_HomePage_Obj.homePage.alert,"Alert message");
		Thread.sleep(3000);

	}
	
	public void navigateToHomPage(String scriptNo, String dataSetNo, String dataSheet) throws Exception {
		ReadExcelFile read = new ReadExcelFile();
		ReusableUtils reuse = new ReusableUtils();
		String dataFileName = "GWR_testdata.xlsx";
		File path = new File("./TestData/" + dataFileName);
		String testDataFile = path.toString();
		//String dataSheet = "CheckIn";
		
		String ProURL=DataMiner.fngetcolvalue(testDataFile, "Config_Sheet", scriptNo, "1", "UrlNavigate");
		Thread.sleep(2000);
		openurl(Configuration.GWR_URL+"/"+ProURL); 
		Thread.sleep(5000);
		reuse.closeCookies();
		Thread.sleep(30000);
		reuse.closeLeadGenpopUp();

	}

}
