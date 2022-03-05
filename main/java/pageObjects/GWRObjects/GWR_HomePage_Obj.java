package pageObjects.GWRObjects;

public class GWR_HomePage_Obj {
	public class homePage{
		
		public static final String emailid="@xpath=//input[@id='email']";
		public static final String pass="@xpath=//input[@id='password']";
		public static final String signInButton="@xpath=(//div[contains(text(),'Sign In')])[2]";
		  public static final String my_account="@xpath=//div[contains(text(),'My Account')]";
		public static final String menuecloseicon="@xpath=//div[@class='menu-icon open']";
		 public static final String UserMenue="@xpath=//div[@class='menu-icon']";
		public static final String login_button="@xpath=//button[contains (text(),'Sign In')]";
		public static final String closeIcon="@xpath=//*[@class='menu-icon open']";
		public static final String bookonroomcard ="@xpath=(//a[@class='button show-for-small-only' and contains(text(),'BOOK')])[1]";
		public static final String deals="@xpath=(//a[contains(text(),'Deals')])[1]";
		public static final String deals2="@xpath=(//a[contains(text(),'Deals')])[2]";

public static final String Apply="@xpath=(//*[@class='show-for-small-only button grid-item__actions--check-availability'])[6]";

public static final String checkin_Esaver="@xpath=(//div[text()='15'])[2]";
public static final String checkout_Esaver ="@xpath=(//div[text()='17'])[2]";
public static final String alert="@xpath=//div[contains(text(),'Unfortunately those dates are outside the range of')]";
		
public static final String Applycode="@xpath=(//div[@class='cmp-button-primary cta-button--center-aligned check-availability-wrapper'])[4]";

public static final String viewofferbtn="@xpath=//*[contains(text(),'VIEW SPECIAL OFFERS')]";
	}

}
