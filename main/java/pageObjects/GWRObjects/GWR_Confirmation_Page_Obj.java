package pageObjects.GWRObjects;

import baseClasses.Configuration;

public class GWR_Confirmation_Page_Obj {

	public class confirmationObj {

		public static final String LocationBtn = "@xpath=(//*[contains(text(), 'Choose A Lodge')])[1]";
		public static final String SouthLocation = "@xpath=//h2[contains(text(),'South')]";
		public static final String SouthWillamsburg = "@xpath=//a[contains(text(),'Williamsburg, VA')]";
		public static final String CheckInDate = "@xpath=//input[@id='checkInOut']";

		public static final String BookNow = "@xpath=//button[contains(text(), 'Book')]";
		public static final String checkIn = "@xpath=(//div[contains(text(), '20')])[24]";
		public static final String checkOut = "@xpath=//div[contains(text(),'16')])[1]";
		public static final String SignUpModalCloseIcon = "@xpath=//*[@alt='Close email capture modal']";
		public static final String Guest = "@xpath=//*[@name='totalGuests']";
		public static final String GuestAdult = "@xpath= //div[@data-testid=\"GuestsCounterAdults\"]//button[@data-testid=\"PlusButton\"]";
		public static final String GuestKid = "@xpath= (//*[@data-testid='GuestsCounterInput'])[1]";
		public static final String KidAgeDD = "@xpath=(//*[select])[3]";
		public static final String KidAge = "@xpath= ";
		public static final String Offer = "@xpath=//*[@name='offerCode']";
		public static final String Feb = "@xpath= //strong[contains(text(),'December 2022')]";
		public static final String DoneBtn = "@xpath= //button[contains(text(),'DONE')]";
		public static final String SubmitBtn = "@xpath=//*[@type='submit']";
		public static final String SetBtn = "@xpath=//button[contains(text(),'Set Dates')]";
		public static final String closeLeadGenPopup = "@xpath=//*[@alt=\"Close email capture modal\"]";
		public static final String closeCookies = "@xpath=//*[@class=\"cookie-notification__close-button\"]";
		public static final String checkInD = "@xpath=//strong[contains(text(),'February 2023')]";
		public static final String check_in = "@xpath=(//div[@class='CalendarMonthGrid CalendarMonthGrid_1 CalendarMonthGrid__vertical_scrollable CalendarMonthGrid__vertical_scrollable_2']//div[9]//div[1]//table//tbody//tr//td)[27]";
		public static final String check_out = "@xpath=(//div[@class='CalendarMonthGrid CalendarMonthGrid_1 CalendarMonthGrid__vertical_scrollable CalendarMonthGrid__vertical_scrollable_2']//div[9]//div[1]//table//tbody//tr//td)[28]";

		// public static final String check_in =
		// "@xpath=(//div[contains(text(),'25')])[2]";
		// public static final String check_out =
		// "@xpath=(//div[contains(text(),'27')])[2]";

		// ***Jan*****
		public static final String check_inmonth = "@xpath=//strong[contains(text(),'January 2023')]";
		public static final String SelectBtn = "@xpath=(//button[contains(text(), 'Select')])[1]";
		public static final String NoThanksBtn = "@xpath=//button[contains(text(),'No thanks')]";
		public static final String PubAttraction = "@xpath=//*[@data-packagecode='PUP']";
		public static final String PubPkgUnderCostSummary = "@xpath=(//*[contains(text(), 'Pup Pass')])[1]";

		public static final String ExpandCostSummary = "@xpath=(//*[@fill=\"currentcolor\"])[1]";
		public static final String QualifyingId = "@xpath=//*[@id='qualifyingIDCode']";

		public static final String pubPass = "@xpath=(//*[contains(text(),'Pup Pass')])[1]";
		public static final String pawPass = "@xpath=//*[@data-packagecode='PAW']";
		public static final String wolfPass = "@xpath=//*[@data-packagecode='WOLF']";

		public static final String costSummryTotalAtHeader = "@xpath=(//div[contains(text(), '$')])[1]";
		public static final String RemoveLink = "@xpath=//div[contains(text(),'REMOVE')]";
		public static final String pkgcost = "@xpath=//div[contains(text(),'Pup Pass')]//following-sibling::div[1]";
		public static final String pubpkgcost = "@xpath=//div[contains(text(),'Paw Pass')]//following-sibling::div[1]";
		public static final String pawpkgcost = "@xpath=//div[contains(text(),'Wolf Pass')]//following-sibling::div[1]";

		public static final String Agree_Book = "@xpath=//button[contains(text(), 'Agree & Book')]";
		public static final String AllDOne = "@xpath=//button[contains(text(),'All Done')]";
		public static final String CheckInOutDateOnCMP = "@xpath=(//*[@fill='#005d78'])[3]//following-sibling::p";
		public static final String GuetCountOnSuite = "@xpath=(//*[@fill=\"#969696\"])[2]//following-sibling::div";

		public static final String checkinoutDateOnSuite = "@xpath=(//*[@fill='#969696'])[1]/following-sibling::div";
		public static final String GeustCountOnCmp = "@xpath=//*[@color='northwoodsGreen']/preceding-sibling::div[1]";
		public static final String CostSummary = "@xpath=//button[contains(text(),'Cost Summary')]";

		public static final String Guestkidage = "@xpath=//select[@name= 'kidsAges.0']";
		public static final String GuestkidplusBtn = "@xpath=//*[@data-testid='GuestsCounterKids']//*[@data-testid='PlusButton']";
		public static final String TotalOnPayment = "@xpath=//div[contains(text(),'TOTAL')]//following-sibling::*";
		public static final String TotalOnCmp = "@xpath=//div[contains(text(),'TOTAL')]//following-sibling::*";

		public static final String costsummary = "@xpath=//button[contains(text(),'Cost Summary')]";
		public static final String suitetype = "@xpath=  (//div[@font-weight='bold'])[11]";

		public static final String unselectedKidage = "@xpath=//select[@name='kidsAges.1']";

		public static final String selectedKidage = "@xpath=//select[@name='kidsAges.0']";
		
		// public static final String arrivalOncmp = "@xpath=
		// "(//p[contains(text(),"We'll see you
		// in")]//following-sibling::div)[2]//*[3]";

	}

	/*
	 * public static void main(String[] args) {
	 * 
	 * for(int i=0;i<4;i++) { String kid="kidsAges."+i; String Guestkidage =
	 * "@xpath=//select[@name= "+"'"+kid+"'"+"]"; System.err.println(Guestkidage); }
	 * 
	 * }
	 * 
	 * public static void main(String[] args) extends se{
	 * 
	 * System.out.println("waiting for dubugger"); openurl(Configuration.OAT_URL);
	 * Thread.sleep(5000); reuse.closeCookies(); Thread.sleep(30000);
	 * reuse.closeLeadGenpopUp();
	 * 
	 * 
	 * verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.
	 * LocationBtn,"Location button");
	 * click(GWR_Confirmation_Page_Obj.confirmationObj.LocationBtn,"Location button"
	 * ); Thread.sleep(5000);
	 * verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.
	 * SouthLocation,"Location button");
	 * click(GWR_Confirmation_Page_Obj.confirmationObj.
	 * SouthLocation,"Location button"); Thread.sleep(5000);
	 * verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.
	 * SouthWillamsburg,"Location button");
	 * click(GWR_Confirmation_Page_Obj.confirmationObj.
	 * SouthWillamsburg,"Location button"); Thread.sleep(8000);
	 * 
	 * 
	 * }
	 */
}
