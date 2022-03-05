package testHarness.GWRFunctions;

import java.io.File;
import java.io.IOException;

import baseClasses.DataMiner;
import baseClasses.ReadExcelFile;
import baseClasses.ReusableUtils;
import baseClasses.SeleniumUtils;
import pageObjects.GWRObjects.BookingEngine_PlanPage_Obj;
import pageObjects.GWRObjects.GWR_Confirmation_Page_Obj;
import pageObjects.GWRObjects.GWR_HomePage_Obj;

public class BookingEngine_PlanPage_Function extends SeleniumUtils { 
	ReusableUtils reuse = new ReusableUtils();
	ReadExcelFile read = new ReadExcelFile();
	GWR_HomePage_Function home = new GWR_HomePage_Function();
	// GWR_HomePage_Function homePage = new GWR_HomePage_Function();

	String dataFileName = "GWR_testdata.xlsx";
	File path = new File("./TestData/" + dataFileName);
	String testDataFile = path.toString();

	public static String checkInOut = null;
	public static String guestCount = null;
	public static String getFirstAdultCount = null;
	public static String getAdultCntAfterDecre = null; 

	public void navigateToPlanThroughBBookNowBtn(String scriptNo, String dataSetNo, String dataSheet) throws Exception {
		Thread.sleep(2000);
		reuse.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");
		click(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest");

	}

	public void selectCheckInOutDates(String scriptNo, String dataSetNo, String dataSheet) throws Exception {
		Thread.sleep(2000);
		home.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");
		click(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");
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
		System.out.println(checkInOut);
		verifyDates();

	}

	public void verifyGuestAbleToIncreaeCount(String scriptNo, String dataSetNo, String dataSheet) throws Exception {
		home.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");
		click(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest");
		click(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest");

		reuse.selectAdults();
		reuse.selectBillableAndNonBillableKids();
		click(GWR_Confirmation_Page_Obj.confirmationObj.DoneBtn, "click Done btn");

		guestCount = reuse.getSendDates(GWR_Confirmation_Page_Obj.confirmationObj.Guest);
		System.out.println(guestCount);
		verifyGuestCount();
	}

	public void verifyGuestAbleToDecreCount(String scriptNo, String dataSetNo, String dataSheet) throws Exception {
		home.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");
		click(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest");
		click(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest");
		decrementSelectedAdult();
		decrementSelectedkid();
	}

	public boolean verifyDates() {
		if (checkInOut != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean verifyGuestCount() {
		if (guestCount != null) {
			return true;
		} else {
			return false;
		}

	}

	public boolean decrementSelectedAdults() throws IOException {
		for (int i = 0; i < 1; i++) {

			click(BookingEngine_PlanPage_Obj.planPage.GuestAdult, "Increment Adult");
			click(BookingEngine_PlanPage_Obj.planPage.GuestAdult, "Increment Adult");
			getFirstAdultCount = reuse.getSendDates(BookingEngine_PlanPage_Obj.planPage.adultValue);
			System.out.println(getFirstAdultCount);
			click(BookingEngine_PlanPage_Obj.planPage.GuestAdultMinus, "Decrement Adult");
			getAdultCntAfterDecre = reuse.getSendDates(BookingEngine_PlanPage_Obj.planPage.adultValue);
			System.out.println(getAdultCntAfterDecre);
			if (getFirstAdultCount != getAdultCntAfterDecre) {
				return true;
			}
		}
		return false;

	}

	public boolean verifyKidDD(String scriptNo, String dataSetNo, String dataSheet) throws Exception {
		Thread.sleep(2000);
		reuse.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");
		click(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest");
		click(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest");

		click(BookingEngine_PlanPage_Obj.planPage.plus2, "plus symbol");
		click(BookingEngine_PlanPage_Obj.planPage.plus2, "plus symbol");
		if (isVisible(BookingEngine_PlanPage_Obj.planPage.kidDD2) == true) {
			System.out.println("Working");
			return true;
		}
		return false;

	} 

	public void verifyKidAge(String scriptNo, String dataSetNo, String dataSheet) throws Exception {
		Thread.sleep(2000);
		reuse.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");
		click(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest");
		click(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest");

		click(BookingEngine_PlanPage_Obj.planPage.plus2, "plus symbol");
		selectByIndex(BookingEngine_PlanPage_Obj.planPage.NonBage, 2, "non billable kid");
		click(GWR_Confirmation_Page_Obj.confirmationObj.DoneBtn, "click Done btn");

	}
	
	public void verifyAlertMsgForMaxGuest(String scriptNo, String dataSetNo, String dataSheet) throws Exception {
		Thread.sleep(2000);
		reuse.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");
		click(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest");
		click(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest");
		for(int i=0; i<=11; i++) {
			click(GWR_Confirmation_Page_Obj.confirmationObj.GuestAdult, "Select Adult");
		}
		verifyExists(BookingEngine_PlanPage_Obj.planPage.alertmsg, "Alert msg");
		
	}
	
	public void verifyAlertMsgForMoreFun(String scriptNo, String dataSetNo, String dataSheet) throws Exception {
		
		String Offer = DataMiner.fngetcolvalue(testDataFile, dataSheet, scriptNo, dataSetNo, "Offer"); 

		Thread.sleep(2000);
		reuse.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		home.loginIntoGWR(scriptNo, dataSetNo, dataSheet);
		verifyExists(GWR_HomePage_Obj.homePage.UserMenue, "icon of menue"); 
		click(GWR_HomePage_Obj.homePage.UserMenue, "icon of menue");
		verifyExists(GWR_HomePage_Obj.homePage.deals, "Deals menue");
		click(GWR_HomePage_Obj.homePage.deals, "Deals menue");
		verifyExists(GWR_HomePage_Obj.homePage.deals2, "Deals sub menue");
		click(GWR_HomePage_Obj.homePage.deals2, "Deals sub menue");
		verifyExists(GWR_HomePage_Obj.homePage.Applycode, "apply code CTA"); 
		click(GWR_HomePage_Obj.homePage.Applycode, "apply code CTA");
		Thread.sleep(3000); 
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest"); 
		click(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest"); 

		reuse.selectAdults();

		click(GWR_Confirmation_Page_Obj.confirmationObj.DoneBtn, "click Done btn");

		

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

		click(GWR_Confirmation_Page_Obj.confirmationObj.SubmitBtn, "booknow submit button click");
		Thread.sleep(3000);
		
		verifyExists(GWR_HomePage_Obj.homePage.viewofferbtn,"Alert message");
		Thread.sleep(3000);

	}
	
	public boolean decrementSelectedAdult() throws IOException {
		

		click(BookingEngine_PlanPage_Obj.planPage.GuestAdult, "Increment Adult");
		click(BookingEngine_PlanPage_Obj.planPage.GuestAdult, "Increment Adult");
		getFirstAdultCount = reuse.getSendDates(BookingEngine_PlanPage_Obj.planPage.Guest);
		System.out.println(getFirstAdultCount);
		click(BookingEngine_PlanPage_Obj.planPage.GuestAdultMinus, "Decrement Adult");
		getAdultCntAfterDecre = reuse.getSendDates(BookingEngine_PlanPage_Obj.planPage.Guest);
		System.out.println(getAdultCntAfterDecre);
		if (getFirstAdultCount != getAdultCntAfterDecre) {
			return true;
		}
	return false;
}
	public boolean decrementSelectedkid() throws IOException, InterruptedException {
		
	  click(BookingEngine_PlanPage_Obj.planPage.plus2,"Increment kid");
	  click(BookingEngine_PlanPage_Obj.planPage.plus2,"Increment kid");
	  verifyExists(BookingEngine_PlanPage_Obj.planPage.NonBage,"Non billable kid");
	  verifyExists(BookingEngine_PlanPage_Obj.planPage.kidDD2," billable kid");
	  verifyExists(BookingEngine_PlanPage_Obj.planPage.GuestAdultMinus,"minus symbol");
	  click(BookingEngine_PlanPage_Obj.planPage.GuestAdultMinus,"minus symbol");
	  if(verifyExists(BookingEngine_PlanPage_Obj.planPage.kidDD2)==false) {
		  return true;
	  }
	 
		return false;
	

}
	

}
