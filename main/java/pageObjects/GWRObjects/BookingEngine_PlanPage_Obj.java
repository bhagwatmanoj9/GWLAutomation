package pageObjects.GWRObjects;

public class BookingEngine_PlanPage_Obj {
	public class planPage{
		
		 public static final String BillKid="@xpath=//select[@name='kidsAges.0']";

		 public static final String NonBillKid="@xpath=//select[@name='kidsAges.1']";
		 public static final String adultValue="@xpath=//*[@data-testid='GuestsCounterInput;]";
		 public static final String GuestAdult="@xpath=//*[@data-testid='GuestsCounterAdults']//*[@data-testid='PlusButton']";
		 public static final String GuestAdultMinus="@xpath=//*[@data-testid='GuestsCounterAdults']//*[@data-testid='MinusButton']";
		  public static final String ageof4="@xpath=//select[@name='kidsAges.5']";
		  public static final String ageof6="@xpath=//select[@name='kidsAges.7']";
		  public static final String plus2="@xpath=//*[@data-testid='GuestsCounterKids']//*[@data-testid='PlusButton']";
		  public static final String NonBage="@xpath=//select[@name='kidsAges.0']";
		  public static final String kidDD2="@xpath=//*[@name='kidsAges.1']";
		  
		  public static final String alertmsg="@xpath=//*[@data-testid='Alert:offericon']";
			public static final String Guest = "@xpath=//*[@name='totalGuests']";

		  
		
	}

}
