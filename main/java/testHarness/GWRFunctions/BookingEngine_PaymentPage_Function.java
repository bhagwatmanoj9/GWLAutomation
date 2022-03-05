package testHarness.GWRFunctions;


import java.io.File;
import java.io.IOException;

import javax.mail.MessagingException;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import baseClasses.DataMiner;
import baseClasses.MailSender;
import baseClasses.ReadExcelFile;
import baseClasses.ReusableUtils;
import baseClasses.SeleniumUtils;
import pageObjects.GWRObjects.BookingEngine_PaymentPage_Obj;
import pageObjects.GWRObjects.FindYourReservation_Obj;
import pageObjects.GWRObjects.GWR_Confirmation_Page_Obj;

public class BookingEngine_PaymentPage_Function extends SeleniumUtils {

	ReadExcelFile read = new ReadExcelFile();
	ReusableUtils reuse = new ReusableUtils();
	MailSender readMail = new MailSender();
	String dataFileName = "GWR_testdata.xlsx";
	File path = new File("./TestData/" + dataFileName);
	String testDataFile = path.toString();
	String dataSheet = "CheckIn";

	public static String reservationNumber = null;
	public static String GuestOnCmp = null; 
	private static String guetCountOnSuite="";
	public static String checkInOut = null; 
	//String reservationNumber = null; 
	//String checkInOut = null; 
	public static String checkInOutOnCmp = null;
	//public static String GuestOnCmp = null;

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
		reuse.writeTofile(FindYourReservation_Obj.findYourReservation.FILEPATH, reservationNumber.substring(13));
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
		System.out.println(getGuetCountOnSuite()); 
		System.out.println(GuestOnCmp); 

		try {
			if (this.guetCountOnSuite.equals(reuse.GuestOnCmp)) {
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
		System.out.println(checkInOutOnCmp); 
		System.out.println(checkInOut);  
		
		if(reuse.checkInOut.equals(checkInOutOnCmp)) {
			System.out.println("Dates are matching");
			return true;
		}
		else {
			System.out.println("Dates are not matching");
			return false;
		}
	}

	public String getGuetCountOnSuite() {
		return guetCountOnSuite;
	}

	public void setGuetCountOnSuite(String guetCountOnSuite) {
		BookingEngine_PaymentPage_Function.guetCountOnSuite = guetCountOnSuite;
	}



}
