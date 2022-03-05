package pageObjects.GWRObjects;

public class BookingEngine_SuitePage_Obj {

	public class suitePage{
		public static final String Roomtype = "@xpath=//button[contains(text(), 'Filter By')]";
		public static final String StandardSuite = "@xpath=//label[@for='standard']";
		public static final String ThemedSuite = "@xpath=//label[@for='themed']";
		public static final String PremiumSuite = "@xpath=//label[@for='premium']";
		public static final String CloseFilter = "@xpath=//*[@fill='#123a5d']";
		public static final String accessibleToggle = "//span[contains(text(),'Accessible Rooms Only')]//following-sibling::*";
		public static final String filterSelected = "@xpath=(//*[contains(text(), 'Filters')])[1]";
		public static final String edit = "@xpath=//button[contains(text(),'Edit')]";
		
		   public static final String updatestay="@xpath=//button[contains(text(),'Update Your Stay')]";
		   public static final String offerdetailcaret="@xpath=//div[text()='Offer Details']";
		    public static final String offerdetailmsg="@xpath=//div[contains(text(),'Gift Certificate Amount Off ')]";

		    public static final String viewmoredetailcaret="@xpath=(//div[@aria-expanded='false'])[1]";
		    public static final String callapseContents="@xpath=//li[contains(text(),'Private master suite with a queen-size bed, 32-inc')]";

		  

		//label[@for='themed']
	}
}
