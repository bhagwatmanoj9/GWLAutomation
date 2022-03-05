package baseClasses;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.mail.MessagingException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import pageObjects.GWRObjects.BookingEngine_ActivitiesPage_Obj;
import pageObjects.GWRObjects.BookingEngine_PaymentPage_Obj;
import pageObjects.GWRObjects.BookingEngine_PlanPage_Obj;
import pageObjects.GWRObjects.BookingEngine_SuitePage_Obj;
import pageObjects.GWRObjects.FindYourReservation_Obj;
import pageObjects.GWRObjects.GWR_Confirmation_Page_Obj;
import testHarness.GWRFunctions.BookingEngine_PaymentPage_Function;

public class ReusableUtils extends SeleniumUtils {
	
	ReadExcelFile read = new ReadExcelFile();
	MailSender readMail = new MailSender();
	//ReusableUtils reuse = new ReusableUtils();

	String dataFileName = "GWR_testdata.xlsx";
	File path = new File("./TestData/" + dataFileName);
	String testDataFile = path.toString();
	String dataSheet = "CheckIn";
	public static String TotalOnPaymentn = null;
	
	public static String GuetCountOnSuite = null;
	public static String checkInOut = null; 
	String reservationNumber = null; 
	//String checkInOut = null; 
	public static String checkInOutOnCmp = null;
	public static String GuestOnCmp = null;

	public static String suite_name = null;
	//public static String suitename = null;
	public static float stringToFloatConvert(String value) {
		try {
			return Float.valueOf(value);
		} catch (NumberFormatException e) {
			System.out.println("Number format Exception for string:" + value); 
			throw e;
		}

	}

	public void addQualifyingId() throws IOException {
		if (verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.QualifyingId) == true) { 
			click(GWR_Confirmation_Page_Obj.confirmationObj.QualifyingId);
			// sendKeys(GWR_Confirmation_Page_Obj.confirmationObj.QualifyingId, "12345",
			// "Adding id");
			sendKeys(GWR_Confirmation_Page_Obj.confirmationObj.QualifyingId, "12345", "Enter Billing Address");

		}

	}

	public void handleLcoPopUp() throws IOException {
		if (isVisible(GWR_Confirmation_Page_Obj.confirmationObj.NoThanksBtn)) {

			click(GWR_Confirmation_Page_Obj.confirmationObj.NoThanksBtn);
		}
	}

	public void verifyPkgAvaialability() throws InterruptedException, IOException {

		if (verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.PubAttraction, "Add Package")) { 
			click(GWR_Confirmation_Page_Obj.confirmationObj.PubAttraction, "Add Package");
			Thread.sleep(3000);

		} else if (verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.pawPass, "Add Package")) { 
			click(GWR_Confirmation_Page_Obj.confirmationObj.pawPass, "Add Package");
			Thread.sleep(3000);

		} else if (verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.wolfPass, "Add Package")) {
			click(GWR_Confirmation_Page_Obj.confirmationObj.wolfPass, "Add Package");
			Thread.sleep(3000);

		} else {
			System.out.println("Packages are not available");
		}
	}

	public boolean closeCookies() throws Exception {
		if (isVisible(GWR_Confirmation_Page_Obj.confirmationObj.closeCookies) == true) {
			click(GWR_Confirmation_Page_Obj.confirmationObj.closeCookies, "Close cookies");
		}

		return false;
	}

	public void closeLeadGenpopUp() throws Exception {
		if (isVisible(GWR_Confirmation_Page_Obj.confirmationObj.closeLeadGenPopup) == true) {
			click(GWR_Confirmation_Page_Obj.confirmationObj.closeLeadGenPopup, "Close lead gen pop up"); 
		}

	}

	public static void writeTofile(String filepath, String resId) {
		File path = new File(filepath);
		try (FileWriter fw = new FileWriter(path, true)) {
			fw.write(resId + ",");
			fw.close();
		} catch (IOException e) {
			System.out.println("exception occured while wrting to file");
		}
		System.out.println("Reservation id wriiten to file successfully" + resId);
	}
	// ./TestData/ReservationData.txt

	public void selectGuest(String locator) throws IOException {

		WebElement fieldName = findWebElement(locator);
		// String attribute = fieldName.getAttribute("id");
		String script = "document.querySelector('input[data-testid=\"GuestsCounterInput\"]').value=6";
		try {
			System.out.println("before value:" + fieldName.getAttribute("value"));
			JavascriptExecutor js = (JavascriptExecutor) webDriver;

			js.executeScript(script, fieldName);
			String fieldNameVal = fieldName.getAttribute("value");
			System.out.println("after value:" + fieldNameVal);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void selectAdults() throws IOException { 
		for (int i = 0; i <= 2; i++) {

			click(GWR_Confirmation_Page_Obj.confirmationObj.GuestAdult, "Select Adult");
		}
	}



	public void selectBillableAndNonBillableKids() throws IOException {
		for (int i = 0; i<1; i++) { 

			click(GWR_Confirmation_Page_Obj.confirmationObj.GuestkidplusBtn, "Select Adult");
			selectByIndex(BookingEngine_PlanPage_Obj.planPage.BillKid,6,"billable kid");
			click(GWR_Confirmation_Page_Obj.confirmationObj.GuestkidplusBtn, "Select Adult");
			selectByIndex(BookingEngine_PlanPage_Obj.planPage.NonBillKid,2,"non billable kid");  
		}
	}
	public void selectNonBillableKids() throws IOException {
		for (int i = 0; i <= 3; i++) {

			click(GWR_Confirmation_Page_Obj.confirmationObj.GuestkidplusBtn, "Select Adult");
		}
	}

	public void navigateToHomePage(String scriptNo, String dataSetNo, String dataSheet) throws Exception {
		ReadExcelFile read = new ReadExcelFile();
		ReusableUtils reuse = new ReusableUtils();
		String dataFileName = "GWR_testdata.xlsx";
		File path = new File("./TestData/" + dataFileName);
		String testDataFile = path.toString();
		//String dataSheet = "CheckIn";
		
		String ProURL=DataMiner.fngetcolvalue(testDataFile, "Config_Sheet", scriptNo, "1", "UrlNavigate");
		
		openurl(Configuration.GWR_URL+"/"+ProURL); 
		Thread.sleep(5000);
		closeCookies();
		Thread.sleep(30000);
		closeLeadGenpopUp();

	}
	
	private void sendDates(String locator) throws IOException {
		WebElement webElement = null;
		// String CheckIn = DataMiner.fngetcolvalue(testDataFile, dataSheet, scriptNo,
		// dataSetNo, "CheckIn");
		String scriptNo = "1";
		String dataSetNo = "2";


		WebElement fieldName = findWebElement(locator);
		String attribute = fieldName.getAttribute("id");
		System.out.println("attribute is:" + attribute);

		String script = "document.getElementById(\"checkInOut\").setAttribute(\"value\",\"Jun.22,2022-Jun.24,2022\")";

		try {
			System.out.println("before value:" + fieldName.getAttribute("value")); 
			JavascriptExecutor js = (JavascriptExecutor) webDriver;

			js.executeScript(script, fieldName);
			String fieldNameVal = fieldName.getAttribute("value");
			System.out.println("after value:" + fieldNameVal);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getSendDates(String locator) throws IOException {

		WebElement fieldName = findWebElement(locator);
		String value = fieldName.getAttribute("value");
		System.out.println("value is:" + value);
		return value;
	
	} 
	
	public void readBookingMail() {
		String driverPath = System.getProperty("user.dir");
		//System.setProperty();
		
	}
	
	public void searchStandardSuite(String scriptNo, String dataSetNo, String dataSheet) throws IOException, InterruptedException {
		String Offer = DataMiner.fngetcolvalue(testDataFile, dataSheet, scriptNo, dataSetNo, "Offer"); 

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");
		click(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest"); 

		selectAdults();

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

		checkInOut = getSendDates(GWR_Confirmation_Page_Obj.confirmationObj.CheckInDate);

		click(GWR_Confirmation_Page_Obj.confirmationObj.SubmitBtn, "booknow submit button click");
		Thread.sleep(7000);

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.GuetCountOnSuite, "CheckInOut Dates");
		GuetCountOnSuite = getTextFrom(GWR_Confirmation_Page_Obj.confirmationObj.GuetCountOnSuite);
		System.out.println(GuetCountOnSuite);		

		addQualifyingId();

		verifyExists(BookingEngine_SuitePage_Obj.suitePage.Roomtype, "Click on Filter Type");
		click(BookingEngine_SuitePage_Obj.suitePage.Roomtype, "Click on Filter Type");
		verifyExists(BookingEngine_SuitePage_Obj.suitePage.StandardSuite, "Select Standard suite");
		click(BookingEngine_SuitePage_Obj.suitePage.StandardSuite, "Select Standard suite");
		click(BookingEngine_SuitePage_Obj.suitePage.CloseFilter, "close Filter Type");
		Thread.sleep(3000); 
	}

	public void searchThemSuite(String scriptNo, String dataSetNo, String dataSheet) throws IOException, InterruptedException {
		String Offer = DataMiner.fngetcolvalue(testDataFile, dataSheet, scriptNo, dataSetNo, "Offer");

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");  
		click(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn"); 
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest");
		click(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest");

		selectAdults();
		//selectBillableAndNonBillableKids();
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

		checkInOut = getSendDates(GWR_Confirmation_Page_Obj.confirmationObj.CheckInDate);

		click(GWR_Confirmation_Page_Obj.confirmationObj.SubmitBtn, "booknow submit button click");
		Thread.sleep(7000);

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.GuetCountOnSuite, "CheckInOut Dates");
		GuetCountOnSuite = getTextFrom(GWR_Confirmation_Page_Obj.confirmationObj.GuetCountOnSuite);
		System.out.println(GuetCountOnSuite);
		
		//setting value in payment
		BookingEngine_PaymentPage_Function bookingEngine_PaymentPage_Function=new BookingEngine_PaymentPage_Function();
		bookingEngine_PaymentPage_Function.setGuetCountOnSuite(GuetCountOnSuite);

		addQualifyingId();

		verifyExists(BookingEngine_SuitePage_Obj.suitePage.Roomtype, "Click on Filter Type");
		click(BookingEngine_SuitePage_Obj.suitePage.Roomtype, "Click on Filter Type");
		verifyExists(BookingEngine_SuitePage_Obj.suitePage.ThemedSuite, "Select theme suite");
		click(BookingEngine_SuitePage_Obj.suitePage.ThemedSuite, "Select theme suite");
		click(BookingEngine_SuitePage_Obj.suitePage.CloseFilter, "close Filter Type");
		Thread.sleep(3000);		
	}
	
	public void searchPremiumSuite(String scriptNo, String dataSetNo, String dataSheet) throws IOException, InterruptedException {
		String Offer = DataMiner.fngetcolvalue(testDataFile, dataSheet, scriptNo, dataSetNo, "Offer");

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");
		click(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest"); 
		click(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest");

		selectAdults();

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
		
		checkInOut = getSendDates(GWR_Confirmation_Page_Obj.confirmationObj.CheckInDate);


		click(GWR_Confirmation_Page_Obj.confirmationObj.SubmitBtn, "booknow submit button click");
		Thread.sleep(7000);

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.GuetCountOnSuite, "CheckInOut Dates");
		GuetCountOnSuite = getTextFrom(GWR_Confirmation_Page_Obj.confirmationObj.GuetCountOnSuite);
		System.out.println(GuetCountOnSuite);		

		addQualifyingId();

		verifyExists(BookingEngine_SuitePage_Obj.suitePage.Roomtype, "Click on Filter Type");
		click(BookingEngine_SuitePage_Obj.suitePage.Roomtype, "Click on Filter Type");
		verifyExists(BookingEngine_SuitePage_Obj.suitePage.PremiumSuite, "Search Premium Suite");
		click(BookingEngine_SuitePage_Obj.suitePage.PremiumSuite, "Search Premium Suite");
		click(BookingEngine_SuitePage_Obj.suitePage.CloseFilter, "close Filter Type");
		Thread.sleep(3000);
		

	}
	
	public void navigateToPaymentFromSuite() throws InterruptedException, IOException{ 
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.SelectBtn, "Select Room");
		click(GWR_Confirmation_Page_Obj.confirmationObj.SelectBtn, "Select Room");
		Thread.sleep(5000);

		handleLcoPopUp();
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
	
	public void addPaymentdetailsForGuestAndBookSuite(String scriptNo, String dataSetNo, String dataSheet) throws IOException, InterruptedException, MessagingException {
		String firstName = DataMiner.fngetcolvalue(testDataFile, dataSheet, scriptNo, dataSetNo, "FirstName"); 
		String lastName = DataMiner.fngetcolvalue(testDataFile, dataSheet, scriptNo, dataSetNo, "LastName");
		String email = DataMiner.fngetcolvalue(testDataFile, dataSheet, scriptNo, dataSetNo, "Email");
		String phoneNumber = DataMiner.fngetcolvalue(testDataFile, dataSheet, scriptNo, dataSetNo, "Phone");
		String CardNumber = DataMiner.fngetcolvalue(testDataFile, dataSheet, scriptNo, dataSetNo, "CardNumber");
		String NameOnCard = DataMiner.fngetcolvalue(testDataFile, dataSheet, scriptNo, dataSetNo, "NameOnCard");
		
		verifyExists(BookingEngine_PaymentPage_Obj.paymentPage.firstName, "Enter firstname");
		click(BookingEngine_PaymentPage_Obj.paymentPage.firstName, "Enter firstname");
		sendKeys(BookingEngine_PaymentPage_Obj.paymentPage.firstName, firstName, "firstName");
		Thread.sleep(3000);

		verifyExists(BookingEngine_PaymentPage_Obj.paymentPage.lastName, "Enter Lastname");
		click(BookingEngine_PaymentPage_Obj.paymentPage.lastName, "Enter Lastname");
		sendKeys(BookingEngine_PaymentPage_Obj.paymentPage.lastName, lastName, "lastName");
		Thread.sleep(3000);

		verifyExists(BookingEngine_PaymentPage_Obj.paymentPage.Email, "Enter Email");
		click(BookingEngine_PaymentPage_Obj.paymentPage.Email, "Enter Email");
		sendKeys(BookingEngine_PaymentPage_Obj.paymentPage.Email, email, "lastName");
		Thread.sleep(3000);

		verifyExists(BookingEngine_PaymentPage_Obj.paymentPage.PhoneNumber, "Enter phone number"); 
		click(BookingEngine_PaymentPage_Obj.paymentPage.PhoneNumber, "Enter phone number");
		sendKeys(BookingEngine_PaymentPage_Obj.paymentPage.PhoneNumber, phoneNumber, "phoneNumber");
		Thread.sleep(3000);

		verifyExists(BookingEngine_PaymentPage_Obj.paymentPage.Continue, "Continue Btn");
		click(BookingEngine_PaymentPage_Obj.paymentPage.Continue, "Continue Btn");
		Thread.sleep(3000);

		verifyExists(BookingEngine_PaymentPage_Obj.paymentPage.NameOnCard, "Enter Name On Card");
		click(BookingEngine_PaymentPage_Obj.paymentPage.NameOnCard, "Enter Name On Card");
		sendKeys(BookingEngine_PaymentPage_Obj.paymentPage.NameOnCard, NameOnCard, "Enter Name On Card");
		Thread.sleep(3000);

		verifyExists(BookingEngine_PaymentPage_Obj.paymentPage.CardNumber, "Enter Name On Card");
		click(BookingEngine_PaymentPage_Obj.paymentPage.CardNumber, "Enter Name On Card");
		sendKeys(BookingEngine_PaymentPage_Obj.paymentPage.CardNumber, CardNumber, "Enter Name On Card");
		Thread.sleep(3000);

		verifyExists(BookingEngine_PaymentPage_Obj.paymentPage.Month, "Enter Month");
		click(BookingEngine_PaymentPage_Obj.paymentPage.Month, "Enter Month");

		Select element = new Select(webDriver.findElement(By.name("paymentOptions.ccMonth")));
		element.selectByValue("05");

		Thread.sleep(3000);
		System.out.println("month:" + element.getFirstSelectedOption().getText()); 

		verifyExists(BookingEngine_PaymentPage_Obj.paymentPage.Year, "Enter Year");
		click(BookingEngine_PaymentPage_Obj.paymentPage.Year, "Enter Year");

		Select year = new Select(webDriver.findElement(By.name("paymentOptions.ccYear")));
		year.selectByValue("2025");
		Thread.sleep(3000);

		System.out.println("payment Year:" + year.getFirstSelectedOption().getText());

		verifyExists(BookingEngine_PaymentPage_Obj.paymentPage.CVV, "Enter CVV");
		click(BookingEngine_PaymentPage_Obj.paymentPage.CVV, "Enter CVV");
		sendKeys(BookingEngine_PaymentPage_Obj.paymentPage.CVV, "202", "CVV");
		Thread.sleep(3000);

		verifyExists(BookingEngine_PaymentPage_Obj.paymentPage.Billingaddress, "Enter Billing address");
		click(BookingEngine_PaymentPage_Obj.paymentPage.Billingaddress, "Enter Billing address");
		sendKeys(BookingEngine_PaymentPage_Obj.paymentPage.Billingaddress, "qa", "Enter Billing address");
		Thread.sleep(3000);

		verifyExists(BookingEngine_PaymentPage_Obj.paymentPage.PostalCode, "Enter Postal Code");
		clear(BookingEngine_PaymentPage_Obj.paymentPage.PostalCode);
		click(BookingEngine_PaymentPage_Obj.paymentPage.PostalCode, "Enter Postal Code");
		sendKeys(BookingEngine_PaymentPage_Obj.paymentPage.PostalCode, "10005", "Enter Postal Code");
		Thread.sleep(6000);

		verifyExists(BookingEngine_PaymentPage_Obj.paymentPage.SaveChangeBtn, "Continue Btn");
		click(BookingEngine_PaymentPage_Obj.paymentPage.SaveChangeBtn, "Continue Btn");
		Thread.sleep(3000);
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.TotalOnPayment, "Guest count");
		TotalOnPaymentn = getTextFrom(GWR_Confirmation_Page_Obj.confirmationObj.TotalOnPayment);
		System.out.println(TotalOnPaymentn);
		Thread.sleep(2000);

		verifyExists(BookingEngine_PaymentPage_Obj.paymentPage.Agree_Book, "Agree & Book Btn");  
		click(BookingEngine_PaymentPage_Obj.paymentPage.Agree_Book, "Agree & Book Btn");
		Thread.sleep(50000);

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.AllDOne, "All Done Btn"); 
		click(GWR_Confirmation_Page_Obj.confirmationObj.AllDOne, "All Done Btn");
		Thread.sleep(3000);

		verifyExists(FindYourReservation_Obj.findYourReservation.ReservationId, "Verify reservation number");
		reservationNumber = getTextFrom(FindYourReservation_Obj.findYourReservation.ReservationId);
		Thread.sleep(3000);

		System.out.println(reservationNumber.substring(13));
		writeTofile(FindYourReservation_Obj.findYourReservation.FILEPATH, reservationNumber.substring(13));
		Thread.sleep(3000);
		
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.GeustCountOnCmp, "Guest count");
		GuestOnCmp = getTextFrom(GWR_Confirmation_Page_Obj.confirmationObj.GeustCountOnCmp);
		System.out.println(GuestOnCmp);
		Thread.sleep(2000);
		
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.CheckInOutDateOnCMP, "Guest count");
		checkInOutOnCmp = getTextFrom(GWR_Confirmation_Page_Obj.confirmationObj.CheckInOutDateOnCMP);
		System.out.println(checkInOutOnCmp); 
		Thread.sleep(10000); 
		//Oct. 21, 2022 - Oct. 22, 2022
		readMail.fetchMail(checkInOutOnCmp.replace(".", "")); 
		
	}
	public void verifyGuestCountOnCmp() {
		System.out.println(GuetCountOnSuite); 
		System.out.println(GuestOnCmp); 

		try {
			if (GuetCountOnSuite.equals(GuestOnCmp)) {
				System.out.println("guest are matching"); 
				//return true; 
			} else {
				System.out.println("guest are not matching");
				//return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean verifyCheckInOutOnCmp() {
		System.out.println(ReusableUtils.checkInOutOnCmp); 
		System.out.println(checkInOut);  
		
		if(checkInOut.equals(ReusableUtils.checkInOutOnCmp)) {
			System.out.println("Dates are matching");
			return true;
		}
		else {
			System.out.println("Dates are not matching");
			return false;
		}
	}
	
	

	}
