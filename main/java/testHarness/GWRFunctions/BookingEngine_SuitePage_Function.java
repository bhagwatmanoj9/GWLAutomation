package testHarness.GWRFunctions;

import java.io.File;
import java.io.IOException;

import baseClasses.DataMiner;
import baseClasses.MailSender;
import baseClasses.ReadExcelFile;
import baseClasses.ReusableUtils;
import baseClasses.SeleniumUtils;
import pageObjects.GWRObjects.BookingEngine_ActivitiesPage_Obj;
import pageObjects.GWRObjects.BookingEngine_SuitePage_Obj;
import pageObjects.GWRObjects.GWR_Confirmation_Page_Obj;

public class BookingEngine_SuitePage_Function extends SeleniumUtils {
	ReadExcelFile read = new ReadExcelFile();
	ReusableUtils reuse = new ReusableUtils();
	MailSender readMail = new MailSender();
	GWR_HomePage_Function home = new GWR_HomePage_Function();

	//GWR_HomePage_Function homePage = new GWR_HomePage_Function();
	BookingEngine_PaymentPage_Function payment = new BookingEngine_PaymentPage_Function();
	String dataFileName = "GWR_testdata.xlsx";
	File path = new File("./TestData/" + dataFileName);
	String testDataFile = path.toString();
	String dataSheet = "CheckIn";

	String reservationNumber = null;
//	public static String checkInOut = null; 
	// public static String checkInOutOnCmp = null;
	String GuestOnCmp = null;
	public static String GuestOnSuite = null;
	String TotalOnPaymentn = null;
	String TotalOnCMP = null;
	public static String suitenameOnCmp = null;
	//public static String TotalOnPaymentn = null;
	
	public static String GuetCountOnSuite = null;
	public static String checkInOut = null; 
	//String reservationNumber = null; 
	//String checkInOut = null; 
	public static String checkInOutOnCmp = null;
	//public static String GuestOnCmp = null;

	public static String suite_name = null;

	public void verifyEditBtn(String scriptNo, String dataSetNo, String dataSheet) throws Exception {
		Thread.sleep(2000);
		//home.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		//reuse.searchThemSuite(scriptNo, dataSetNo, dataSheet);
		verifyExists(BookingEngine_SuitePage_Obj.suitePage.edit, "edit button");
		click(BookingEngine_SuitePage_Obj.suitePage.edit, "edit button");
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.Offer, "Offer");
	}


	public void verifyPlanFields(String scriptNo, String dataSetNo, String dataSheet) throws Exception {
		Thread.sleep(2000);
		home.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		reuse.searchThemSuite(scriptNo, dataSetNo, dataSheet);
		verifyExists(BookingEngine_SuitePage_Obj.suitePage.edit, "edit button");
		click(BookingEngine_SuitePage_Obj.suitePage.edit, "edit button");
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.Offer, "Offer");
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.CheckInDate, "select date field dates");
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest");

	}
	
	public void verifyUpdateYorStayBtn(String scriptNo, String dataSetNo, String dataSheet) throws Exception {
		Thread.sleep(2000);
		home.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		reuse.searchThemSuite(scriptNo, dataSetNo, dataSheet);
		verifyExists(BookingEngine_SuitePage_Obj.suitePage.edit, "edit button");
		click(BookingEngine_SuitePage_Obj.suitePage.edit, "edit button");
		verifyExists(BookingEngine_SuitePage_Obj.suitePage.updatestay, "edit button");

	}

	public boolean verifyOfferDetails(String scriptNo, String dataSetNo, String dataSheet) throws Exception {
		Thread.sleep(2000);
		home.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		reuse.searchThemSuite(scriptNo, dataSetNo, dataSheet);
		click(BookingEngine_SuitePage_Obj.suitePage.edit, "edit button");
		verifyExists(BookingEngine_SuitePage_Obj.suitePage.updatestay, "edit button");

		verifyExists(BookingEngine_SuitePage_Obj.suitePage.offerdetailcaret, "Offer Detail expand");
		click(BookingEngine_SuitePage_Obj.suitePage.offerdetailcaret, "Offer Detail expand");
		if(isVisible(BookingEngine_SuitePage_Obj.suitePage.offerdetailmsg)==false) { 
			return true;
		} 
		return false;
	
	}

	
	public boolean verifySeeMoreWhenHide(String scriptNo, String dataSetNo, String dataSheet) throws Exception {
		
		Thread.sleep(2000);
		home.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		reuse.searchThemSuite(scriptNo, dataSetNo, dataSheet);
		Thread.sleep(2000);
		verifyExists(BookingEngine_SuitePage_Obj.suitePage.viewmoredetailcaret, "Offer Detail expand");
		if(isVisible(BookingEngine_SuitePage_Obj.suitePage.callapseContents)==false) {
			return true;
		}
		return false;
		
	}
	
	
	
	public boolean verifySeeMoreDetails(String scriptNo, String dataSetNo, String dataSheet) throws Exception {
		
		Thread.sleep(2000);
		home.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		reuse.searchThemSuite(scriptNo, dataSetNo, dataSheet);
		Thread.sleep(2000);
		verifyExists(BookingEngine_SuitePage_Obj.suitePage.viewmoredetailcaret, "Offer Detail expand");
		click(BookingEngine_SuitePage_Obj.suitePage.viewmoredetailcaret, "Offer Detail expand");

		if(isVisible(BookingEngine_SuitePage_Obj.suitePage.callapseContents)==true) {
			return true;
		}
		return false;
		
	}
	
	public boolean verifySeeMoreDetailsGetHideAfterClick(String scriptNo, String dataSetNo, String dataSheet) throws Exception {
		
		Thread.sleep(2000);
		home.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		reuse.searchThemSuite(scriptNo, dataSetNo, dataSheet);
		Thread.sleep(2000);
		verifyExists(BookingEngine_SuitePage_Obj.suitePage.viewmoredetailcaret, "Offer Detail expand");
		click(BookingEngine_SuitePage_Obj.suitePage.viewmoredetailcaret, "Offer Detail expand");
		
		click(BookingEngine_SuitePage_Obj.suitePage.viewmoredetailcaret, "Offer Detail expand");

		if(isVisible(BookingEngine_SuitePage_Obj.suitePage.callapseContents)==false) {
			return true;
		}
		return false;
		
	}
	
	public void searchStandardSuite(String scriptNo, String dataSetNo, String dataSheet) throws IOException, InterruptedException {
		String Offer = DataMiner.fngetcolvalue(testDataFile, dataSheet, scriptNo, dataSetNo, "Offer"); 

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");
		click(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest"); 
		click(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest"); 

		reuse.selectAdults();

		click(GWR_Confirmation_Page_Obj.confirmationObj.DoneBtn, "click Done btn");

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.Offer, "Offer");
		sendKeys(GWR_Confirmation_Page_Obj.confirmationObj.Offer, Offer, "Offer");
		clear(GWR_Confirmation_Page_Obj.confirmationObj.Offer);
		sendKeys(GWR_Confirmation_Page_Obj.confirmationObj.Offer, Offer, "Offer");

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.CheckInDate, "select date field dates");
		click(GWR_Confirmation_Page_Obj.confirmationObj.CheckInDate, "select date field dates");

		// sendDates(GWR_Confirmation_Page_Obj.confirmationObj.CheckInDate);

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.check_in, "select check in dates");
		click(GWR_Confirmation_Page_Obj.confirmationObj.check_in, "select check in dates");

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.check_out, "select check out dates"); 
		click(GWR_Confirmation_Page_Obj.confirmationObj.check_out, "select check out dates");

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.SetBtn, "CLick on sublit btn");
		click(GWR_Confirmation_Page_Obj.confirmationObj.SetBtn, "CLick on sublit btn");
		Thread.sleep(5000);

		checkInOut = reuse.getSendDates(GWR_Confirmation_Page_Obj.confirmationObj.CheckInDate);

		click(GWR_Confirmation_Page_Obj.confirmationObj.SubmitBtn, "booknow submit button click");
		Thread.sleep(7000);

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.GuetCountOnSuite, "CheckInOut Dates");
		GuetCountOnSuite = getTextFrom(GWR_Confirmation_Page_Obj.confirmationObj.GuetCountOnSuite);
		System.out.println(GuetCountOnSuite);		

		reuse.addQualifyingId();

		verifyExists(BookingEngine_SuitePage_Obj.suitePage.Roomtype, "Click on Filter Type");
		click(BookingEngine_SuitePage_Obj.suitePage.Roomtype, "Click on Filter Type");
		verifyExists(BookingEngine_SuitePage_Obj.suitePage.StandardSuite, "Select Standard suite");
		click(BookingEngine_SuitePage_Obj.suitePage.StandardSuite, "Select Standard suite");
		click(BookingEngine_SuitePage_Obj.suitePage.CloseFilter, "close Filter Type");
		Thread.sleep(3000); 
	}
	
	public void navigateToPaymentFromSuite() throws InterruptedException, IOException{ 
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.SelectBtn, "Select Room");
		click(GWR_Confirmation_Page_Obj.confirmationObj.SelectBtn, "Select Room");
		Thread.sleep(5000);

		reuse.handleLcoPopUp();
		Thread.sleep(5000);
		
		verifyExists(BookingEngine_ActivitiesPage_Obj.activitiesPage.careticon,"careticon");
		click(BookingEngine_ActivitiesPage_Obj.activitiesPage.careticon,"careticon");
		verifyExists(BookingEngine_ActivitiesPage_Obj.activitiesPage.suite,"suite name ");
		 suite_name=getTextFrom(BookingEngine_ActivitiesPage_Obj.activitiesPage.suite,"suite name");
		System.out.println(suite_name);

		verifyExists(BookingEngine_ActivitiesPage_Obj.activitiesPage.ContinueToDining,
				"Click on continue to dining Btn");
		click(BookingEngine_ActivitiesPage_Obj.activitiesPage.ContinueToDining, "Click on continue to dining Btn");
		Thread.sleep(3000);

		verifyExists(BookingEngine_ActivitiesPage_Obj.activitiesPage.ContinueToPayment,
				"Click on continue to payment Btn");
		click(BookingEngine_ActivitiesPage_Obj.activitiesPage.ContinueToPayment, "Click on continue to payment Btn");
		Thread.sleep(3000);
	}
	
}
	
		
	

	

