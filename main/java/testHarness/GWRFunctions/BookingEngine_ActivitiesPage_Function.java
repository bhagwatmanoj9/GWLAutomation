package testHarness.GWRFunctions;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import baseClasses.Configuration;
import baseClasses.DataMiner;
import baseClasses.ReadExcelFile;
import baseClasses.ReusableUtils;
import baseClasses.SeleniumUtils;
import pageObjects.GWRObjects.GWR_Confirmation_Page_Obj;

public class BookingEngine_ActivitiesPage_Function extends SeleniumUtils {

	ReadExcelFile read = new ReadExcelFile();
	ReusableUtils reuse = new ReusableUtils();
	String dataFileName = "OAT_testdata.xlsx";
	File path = new File("./TestData/" + dataFileName);
	String testDataFile = path.toString();
	String dataSheet = "CheckIn";

	private String packageNameBeforeAdd;
	private String packageNameAfterAdd;
	private String puppackageCost;
	private String pawpackageCost;
	private String wolfpackageCost;

	private String totalCostBeforeUpdatePkg;
	private String totalCostAfterUpdatePkg;

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

	public void verifyCostSummaryAfterPkg(String scriptNo, String dataSetNo, String dataSheet) throws Exception {

		String Offer = DataMiner.fngetcolvalue(testDataFile, dataSheet, scriptNo, dataSetNo, "Offer");
		String billableKid = DataMiner.fngetcolvalue(testDataFile, dataSheet, scriptNo, dataSetNo, "Billable");

		System.out.println("waiting for dubugger");
		openurl(Configuration.GWR_URL);
		Thread.sleep(5000);
		closeCookies();
		Thread.sleep(30000);
		closeLeadGenpopUp();
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");
		click(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.Offer, "Offer");
		sendKeys(GWR_Confirmation_Page_Obj.confirmationObj.Offer, Offer, "Offer");

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.CheckInDate, "click on select dates");
		click(GWR_Confirmation_Page_Obj.confirmationObj.CheckInDate, "click on select dates");

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.check_in, "select check in dates");
		click(GWR_Confirmation_Page_Obj.confirmationObj.check_in, "select check in dates");

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.check_out, "select check in dates");
		click(GWR_Confirmation_Page_Obj.confirmationObj.check_out, "select check in dates");

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.SetBtn, "select dates");
		click(GWR_Confirmation_Page_Obj.confirmationObj.SetBtn, "select dates");
 
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest");
		click(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest");
	//	selectGuest();
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.GuestAdult, "Select Adult");
		click(GWR_Confirmation_Page_Obj.confirmationObj.GuestAdult, "Select Adult");

		click(GWR_Confirmation_Page_Obj.confirmationObj.DoneBtn, "click Done btn");

		click(GWR_Confirmation_Page_Obj.confirmationObj.SubmitBtn, "select Book Bow Btn");
		Thread.sleep(7000);

		reuse.addQualifyingId();
		Thread.sleep(5000); 

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.SelectBtn, "Select Room"); 
		click(GWR_Confirmation_Page_Obj.confirmationObj.SelectBtn, "Select Room");
		Thread.sleep(5000);

		reuse.handleLcoPopUp();
		Thread.sleep(6000);

		puppackageCost = getTextFrom(GWR_Confirmation_Page_Obj.confirmationObj.pkgcost);
		totalCostBeforeUpdatePkg = getTextFrom(GWR_Confirmation_Page_Obj.confirmationObj.costSummryTotalAtHeader);

		System.out.println("packageCost = " + puppackageCost);
		System.out.println("totalCostBeforeUpdatePkg = " + totalCostBeforeUpdatePkg);

		try {
			float pkgCostAfterAddPkgFinal1 = ReusableUtils.stringToFloatConvert(puppackageCost.substring(1))
					+ ReusableUtils.stringToFloatConvert(totalCostBeforeUpdatePkg.substring(1));
			System.out.println(pkgCostAfterAddPkgFinal1);

			reuse.verifyPkgAvaialability();

			totalCostAfterUpdatePkg = getTextFrom(GWR_Confirmation_Page_Obj.confirmationObj.costSummryTotalAtHeader);

			verifyTotalAfterAddActiPkg(pkgCostAfterAddPkgFinal1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void verifyCostSummaryAgainstMulPkg(String scriptNo, String dataSetNo, String dataSheet) throws Exception {

		String Offer = DataMiner.fngetcolvalue(testDataFile, dataSheet, scriptNo, dataSetNo, "Offer");
		String billableKid = DataMiner.fngetcolvalue(testDataFile, dataSheet, scriptNo, dataSetNo, "Billable");

		System.out.println("waiting for dubugger");
		openurl(Configuration.GWR_URL);
		Thread.sleep(5000);
		closeCookies();
		Thread.sleep(30000);
		closeLeadGenpopUp();
		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");
		click(GWR_Confirmation_Page_Obj.confirmationObj.BookNow, "BookNowBtn");

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.Offer, "Offer");
		sendKeys(GWR_Confirmation_Page_Obj.confirmationObj.Offer, Offer, "Offer");

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.CheckInDate, "click on select dates");
		click(GWR_Confirmation_Page_Obj.confirmationObj.CheckInDate, "click on select dates");

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.check_in, "select check in dates");
		click(GWR_Confirmation_Page_Obj.confirmationObj.check_in, "select check in dates");

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.check_out, "select check in dates");
		click(GWR_Confirmation_Page_Obj.confirmationObj.check_out, "select check in dates");

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.SetBtn, "select dates");
		click(GWR_Confirmation_Page_Obj.confirmationObj.SetBtn, "select dates");

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest");
		click(GWR_Confirmation_Page_Obj.confirmationObj.Guest, "Click on guest");

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.GuestAdult, "Select Adult");
		click(GWR_Confirmation_Page_Obj.confirmationObj.GuestAdult, "Select Adult");

		click(GWR_Confirmation_Page_Obj.confirmationObj.DoneBtn, "click Done btn");

		click(GWR_Confirmation_Page_Obj.confirmationObj.SubmitBtn, "select Book Bow Btn"); 
		Thread.sleep(7000);

		reuse.addQualifyingId();
		Thread.sleep(5000);

		verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.SelectBtn, "Select Room");
		click(GWR_Confirmation_Page_Obj.confirmationObj.SelectBtn, "Select Room");
		Thread.sleep(5000);

		reuse.handleLcoPopUp();
		Thread.sleep(6000);

		puppackageCost = getTextFrom(GWR_Confirmation_Page_Obj.confirmationObj.pkgcost);
		pawpackageCost = getTextFrom(GWR_Confirmation_Page_Obj.confirmationObj.pubpkgcost);
		wolfpackageCost = getTextFrom(GWR_Confirmation_Page_Obj.confirmationObj.pawpkgcost);

		totalCostBeforeUpdatePkg = getTextFrom(GWR_Confirmation_Page_Obj.confirmationObj.costSummryTotalAtHeader);

		System.out.println("packageCost = " + puppackageCost);
		System.out.println("packageCost = " + pawpackageCost);
		System.out.println("packageCost = " + wolfpackageCost);
		System.out.println("totalCostBeforeUpdatePkg = " + totalCostBeforeUpdatePkg);

		try {
			float pkgCostAfterAddPkgFinal1 = ReusableUtils.stringToFloatConvert(puppackageCost.substring(1))
					+ ReusableUtils.stringToFloatConvert(totalCostBeforeUpdatePkg.substring(1))
					+ ReusableUtils.stringToFloatConvert(pawpackageCost.substring(1))
					+ ReusableUtils.stringToFloatConvert(wolfpackageCost.substring(1));
			System.out.println(pkgCostAfterAddPkgFinal1);

			// reuse.verifyPkgAvaialability();
			verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.PubAttraction, "Add Package");
			click(GWR_Confirmation_Page_Obj.confirmationObj.PubAttraction, "Add Package");
			Thread.sleep(3000);

			verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.pawPass, "Add Package");
			click(GWR_Confirmation_Page_Obj.confirmationObj.pawPass, "Add Package");
			Thread.sleep(3000);

			verifyExists(GWR_Confirmation_Page_Obj.confirmationObj.wolfPass, "Add Package");
			click(GWR_Confirmation_Page_Obj.confirmationObj.wolfPass, "Add Package");
			Thread.sleep(3000);

			totalCostAfterUpdatePkg = getTextFrom(GWR_Confirmation_Page_Obj.confirmationObj.costSummryTotalAtHeader);

			verifyTotalAfterAddActiPkg(pkgCostAfterAddPkgFinal1);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void verifyTotalAfterAddActiPkg(float pkgCostAfterAddPkgFinal1) {
		System.out.println("pkgCostAfterAddPkgFinal1 = " + pkgCostAfterAddPkgFinal1);
		System.out.println("TotalCostAfterUpdatePkg = " + totalCostAfterUpdatePkg);

		if (pkgCostAfterAddPkgFinal1 == ReusableUtils.stringToFloatConvert(totalCostAfterUpdatePkg.substring(1))) {
			System.out.println("Total is correct");
		}
	}
 
	private boolean verifyAddedPkg() {
		if (packageNameBeforeAdd.equals(packageNameAfterAdd)) {
			return true;
		} else {
			return false;
		}
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
	
	public void selectGuest(String locator) throws IOException {
		
		WebElement fieldName = findWebElement(locator);
		//String attribute = fieldName.getAttribute("id");
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

}
