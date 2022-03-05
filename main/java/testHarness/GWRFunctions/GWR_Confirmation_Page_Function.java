package testHarness.GWRFunctions;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import baseClasses.Configuration;
import baseClasses.DataMiner;
import baseClasses.MailSender;
import baseClasses.ReadExcelFile;
import baseClasses.ReusableUtils;
import baseClasses.SeleniumUtils;
import pageObjects.GWRObjects.BookingEngine_ActivitiesPage_Obj;
import pageObjects.GWRObjects.BookingEngine_PaymentPage_Obj;
import pageObjects.GWRObjects.BookingEngine_SuitePage_Obj;
import pageObjects.GWRObjects.FindYourReservation_Obj;
import pageObjects.GWRObjects.GWR_Confirmation_Page_Obj;
import pageObjects.GWRObjects.GWR_HomePage_Obj;

public class GWR_Confirmation_Page_Function extends SeleniumUtils { 

	ReadExcelFile read = new ReadExcelFile();
	ReusableUtils reuse = new ReusableUtils();
	MailSender readMail = new MailSender();
	GWR_HomePage_Function homePage = new GWR_HomePage_Function();
	BookingEngine_PaymentPage_Function payment = new BookingEngine_PaymentPage_Function();
	BookingEngine_SuitePage_Function suite = new BookingEngine_SuitePage_Function();
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

	public void navigateToConfirmationAsGuestWithStandSuite(String scriptNo, String dataSetNo, String dataSheet)
			throws Exception {

		Thread.sleep(2000);
		homePage.navigateToHomPage(scriptNo, dataSetNo, dataSheet);
		suite.searchStandardSuite(scriptNo, dataSetNo, dataSheet); 
		suite.navigateToPaymentFromSuite();
		payment.addPaymentdetailsForGuestAndBookSuite(scriptNo, dataSetNo, dataSheet);
	}

	public void navigateToConfirmationAsGuestWithThemSuite(String scriptNo, String dataSetNo, String dataSheet)
			throws Exception {

		Thread.sleep(2000);
		reuse.navigateToHomePage(scriptNo, dataSetNo, dataSheet); 
		reuse.searchThemSuite(scriptNo, dataSetNo, dataSheet);
		reuse.navigateToPaymentFromSuite();
		reuse.addPaymentdetailsForGuestAndBookSuite(scriptNo, dataSetNo, dataSheet);
	}

	public void navigateToConfirmationAsGuestWithPremiumSuite(String scriptNo, String dataSetNo, String dataSheet)
			throws Exception {
		Thread.sleep(2000);
		reuse.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		reuse.searchPremiumSuite(scriptNo, dataSetNo, dataSheet);
		reuse.navigateToPaymentFromSuite();
		payment.addPaymentdetailsForGuestAndBookSuite(scriptNo, dataSetNo, dataSheet); 
	}

	public void verifyheckInOutaOnCmpforPremiumSuite(String scriptNo, String dataSetNo, String dataSheet)
			throws Exception {

		Thread.sleep(2000);
		reuse.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		reuse.searchPremiumSuite(scriptNo, dataSetNo, dataSheet); 
		reuse.navigateToPaymentFromSuite();
		payment.addPaymentdetailsForGuestAndBookSuite(scriptNo, dataSetNo, dataSheet);

	}

	public void verifyCheckInOutaOnCmpforThemeSuite(String scriptNo, String dataSetNo, String dataSheet) 
			throws Exception {

		Thread.sleep(2000);
		reuse.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		reuse.searchThemSuite(scriptNo, dataSetNo, dataSheet);
		reuse.navigateToPaymentFromSuite();
		payment.addPaymentdetailsForGuestAndBookSuite(scriptNo, dataSetNo, dataSheet);
	}

	public void verifyCheckInOutOnCmpforStandardSuite(String scriptNo, String dataSetNo, String dataSheet)
			throws Exception {
		Thread.sleep(2000);
		reuse.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		reuse.searchStandardSuite(scriptNo, dataSetNo, dataSheet);
		reuse.navigateToPaymentFromSuite();
		payment.addPaymentdetailsForGuestAndBookSuite(scriptNo, dataSetNo, dataSheet);

	}

	public void bookReserAsGuestWithStandSuite(String scriptNo, String dataSetNo, String dataSheet) throws Exception {

		Thread.sleep(2000);
		reuse.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		reuse.searchStandardSuite(scriptNo, dataSetNo, dataSheet);
		reuse.navigateToPaymentFromSuite();
		reuse.addPaymentdetailsForGuestAndBookSuite(scriptNo, dataSetNo, dataSheet);
		click(GWR_Confirmation_Page_Obj.confirmationObj.costsummary, "cost summary CTA");
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.suitetype, "suite name ");
		suitenameOnCmp = getTextFrom(GWR_Confirmation_Page_Obj.confirmationObj.suitetype, "suite name");
		System.out.println(suitenameOnCmp);
		// verifySelectedsuite();

	}

	public void bookReserAsGuestWithThemSuite(String scriptNo, String dataSetNo, String dataSheet) throws Exception {

		Thread.sleep(2000);
		reuse.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		reuse.searchThemSuite(scriptNo, dataSetNo, dataSheet);
		reuse.navigateToPaymentFromSuite();
		reuse.addPaymentdetailsForGuestAndBookSuite(scriptNo, dataSetNo, dataSheet);
		click(GWR_Confirmation_Page_Obj.confirmationObj.costsummary, "cost summary CTA");
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.suitetype, "suite name ");
		suitenameOnCmp = getTextFrom(GWR_Confirmation_Page_Obj.confirmationObj.suitetype, "suite name");
		System.out.println(suitenameOnCmp);
		// verifySelectedsuite();

	}

	public void bookReserAsGuestWithPremiSuite(String scriptNo, String dataSetNo, String dataSheet) throws Exception {

		Thread.sleep(2000);
		reuse.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		reuse.searchPremiumSuite(scriptNo, dataSetNo, dataSheet);
		reuse.navigateToPaymentFromSuite();
		reuse.addPaymentdetailsForGuestAndBookSuite(scriptNo, dataSetNo, dataSheet);
		click(GWR_Confirmation_Page_Obj.confirmationObj.costsummary, "cost summary CTA");
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.suitetype, "suite name ");
		suitenameOnCmp = getTextFrom(GWR_Confirmation_Page_Obj.confirmationObj.suitetype, "suite name");
		System.out.println(suitenameOnCmp);
		// verifySelectedsuite();

	}

	public void bookAccessibleSuitewithLogUsr(String scriptNo, String dataSetNo, String dataSheet) throws Exception {

		Thread.sleep(2000);
		reuse.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		homePage.loginIntoGWR(scriptNo, dataSetNo, dataSheet);
		reuse.searchThemSuite(scriptNo, dataSetNo, dataSheet);
		accessibleSuite();
		reuse.navigateToPaymentFromSuite(); 
		reuse.addPaymentdetailsForGuestAndBookSuite(scriptNo, dataSetNo, dataSheet);
		click(GWR_Confirmation_Page_Obj.confirmationObj.costsummary, "cost summary CTA");
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.TotalOnCmp, "Guest count");
		TotalOnCMP = getTextFrom(GWR_Confirmation_Page_Obj.confirmationObj.TotalOnCmp);
		System.out.println(TotalOnCMP);

	} 
	
	public void bookAccessibleSuitewithGuestUsrWithAdultOnly(String scriptNo, String dataSetNo, String dataSheet) throws Exception {

		Thread.sleep(2000);
		reuse.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		reuse.searchThemSuite(scriptNo, dataSetNo, dataSheet);
		accessibleSuite();
		reuse.navigateToPaymentFromSuite(); 
		reuse.addPaymentdetailsForGuestAndBookSuite(scriptNo, dataSetNo, dataSheet);
		click(GWR_Confirmation_Page_Obj.confirmationObj.costsummary, "cost summary CTA");
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.TotalOnCmp, "Guest count");
		TotalOnCMP = getTextFrom(GWR_Confirmation_Page_Obj.confirmationObj.TotalOnCmp);
		System.out.println(TotalOnCMP);

	}
	
	public void bookAccessibleSuitewithLogUsrWithAdultOnly(String scriptNo, String dataSetNo, String dataSheet) throws Exception {

		Thread.sleep(2000);
		reuse.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		homePage.loginIntoGWR(scriptNo, dataSetNo, dataSheet);
		reuse.searchThemSuite(scriptNo, dataSetNo, dataSheet);
		accessibleSuite();
		reuse.navigateToPaymentFromSuite(); 
		reuse.addPaymentdetailsForGuestAndBookSuite(scriptNo, dataSetNo, dataSheet);
		click(GWR_Confirmation_Page_Obj.confirmationObj.costsummary, "cost summary CTA");
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.TotalOnCmp, "Guest count");
		TotalOnCMP = getTextFrom(GWR_Confirmation_Page_Obj.confirmationObj.TotalOnCmp);
		System.out.println(TotalOnCMP);

	}
	



	public void verifyTotalBtnPayAndCmpForStand(String scriptNo, String dataSetNo, String dataSheet) throws Exception {

		Thread.sleep(2000);
		reuse.navigateToHomePage(scriptNo, dataSetNo, dataSheet);
		reuse.searchThemSuite(scriptNo, dataSetNo, dataSheet);
		reuse.navigateToPaymentFromSuite();
		reuse.addPaymentdetailsForGuestAndBookSuite(scriptNo, dataSetNo, dataSheet);
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.CostSummary, "Cost Summary");
		click(GWR_Confirmation_Page_Obj.confirmationObj.CostSummary, "Cost Summary");
		Thread.sleep(3000);
		// CheckInOutDateOnCMP
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.TotalOnCmp, "Guest count");
		TotalOnCMP = getTextFrom(GWR_Confirmation_Page_Obj.confirmationObj.TotalOnCmp);
		System.out.println(TotalOnCMP);
		Thread.sleep(2000);
		//verifyTotalOnCmp();
		// verifyGuestCountOnCmp();
		Thread.sleep(3000);
		readMail.fetchTotalFromMail(TotalOnCMP); 


	}

	public void writeReservationNumber(String scriptNo, String dataSetNo, String dataSheet) throws Exception {

		System.out.println("waiting for dubugger");
		openurl(Configuration.GWR_URL);
		Thread.sleep(5000);
		reuse.closeCookies();
		Thread.sleep(30000);
		reuse.closeLeadGenpopUp();

		verifyExists(FindYourReservation_Obj.findYourReservation.UserMenu, "Click on Usermenu");
		click(FindYourReservation_Obj.findYourReservation.UserMenu, "Click on Usermenu");
		Thread.sleep(3000);

		verifyExists(FindYourReservation_Obj.findYourReservation.FindMyReservation, "Click on Find Your Reservation");
		click(FindYourReservation_Obj.findYourReservation.FindMyReservation, "Click on Find Your Reservation");
		Thread.sleep(3000);

		verifyExists(FindYourReservation_Obj.findYourReservation.ReservationField, "Click on Reservation Id field");
		click(FindYourReservation_Obj.findYourReservation.ReservationField, "Click on Reservation Id field");
		sendKeys(FindYourReservation_Obj.findYourReservation.ReservationField, "33931132",
				"Click on Reservation Id field");
		Thread.sleep(3000);

		verifyExists(FindYourReservation_Obj.findYourReservation.Lastname, "Click on Lastname field");
		click(FindYourReservation_Obj.findYourReservation.Lastname, "Click on Lastname field");
		sendKeys(FindYourReservation_Obj.findYourReservation.Lastname, "Nextgenqa", "Click on Lastname field");
		Thread.sleep(3000);

		verifyExists(FindYourReservation_Obj.findYourReservation.SearchBtn, "Click on search btn");
		click(FindYourReservation_Obj.findYourReservation.SearchBtn, "Click on search btn");
		Thread.sleep(5000);

		verifyExists(FindYourReservation_Obj.findYourReservation.ReservationId, "Verify reservation number");

		reservationNumber = getTextFrom(FindYourReservation_Obj.findYourReservation.ReservationId);
		System.out.println(reservationNumber.substring(13));
		reuse.writeTofile(FindYourReservation_Obj.findYourReservation.FILEPATH, reservationNumber.substring(13));
		// FILEPATH

	}

	public boolean verifyGuestCountOnCmp() {
		System.out.println(suite.GuetCountOnSuite);
		System.out.println(payment.checkInOut);

		if (suite.GuetCountOnSuite.equals(payment.GuestOnCmp)) {
			System.out.println("guest are matching");
			return true;
		} else {
			System.out.println("guest are not matching");
			return false;
		}
	}

	public boolean verifyCheckInOutOnCmp() {
		System.out.println(reuse.checkInOutOnCmp);
		System.out.println(reuse.checkInOut);

		if (reuse.checkInOut.equals(reuse.checkInOutOnCmp)) {
			System.out.println("Dates are matching");
			return true;
		} else {
			System.out.println("Dates are not matching"); 
			return false;
		}
	}

	public boolean verifyTotalOnCmp() {
		System.out.println(reuse.TotalOnPaymentn);
		System.out.println(TotalOnCMP);
		if (reuse.TotalOnPaymentn.equals(TotalOnCMP)) {
			return true;

		} else {
			System.out.println("Total are not matching");
			return false;
		}

	}

	public void verifySelectedsuite() {
		System.out.println(reuse.suite_name);
		System.out.println(suitenameOnCmp);

		if (reuse.suite_name.equalsIgnoreCase(suitenameOnCmp)) {
			System.out.println("successfully book resrvation with selected suite");
		} else {
			System.out.println("booking is not done with selected suite");
		}
	}

	public void accessibleSuite() throws InterruptedException, IOException {
		verifyExists(BookingEngine_SuitePage_Obj.suitePage.filterSelected, "Click on Filter Type");
		click(BookingEngine_SuitePage_Obj.suitePage.filterSelected, "Click on Filter Type");
		verifyExists(BookingEngine_SuitePage_Obj.suitePage.accessibleToggle, "Select Standard suite");
		click(BookingEngine_SuitePage_Obj.suitePage.accessibleToggle, "Select Standard suite");
		click(BookingEngine_SuitePage_Obj.suitePage.CloseFilter, "close Filter Type");
		Thread.sleep(3000);

	}

}
