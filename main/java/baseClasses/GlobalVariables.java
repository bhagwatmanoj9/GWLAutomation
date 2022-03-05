package baseClasses;

import java.util.HashMap;

public class GlobalVariables {
	private static String RelativePath = null;
	private static String SummaryReportFile = null;
	private static String TestCaseReportFile = null;
	private static String ResultPath = null;
	private static boolean VerificationFlag = true;
	private static String browser = null;
	private static String gridMode = null;
	private static String hubAddress=null;

	private static String inputExcelFilePath = null;
	private static String inputexcelFileName = null;
	private static String inputExcelSheetName = null;

	private static String outputExcelFilePath = null;
	private static String outputExcelFileName = null;
	private static String currentTestcase = null;
	private static String Environment = null;


	private static String email=null;
	private static String password=null;
	private static String TestSuitePath=null;

	private static String testEmail=null;
	private static String testUsername=null;
	private static String invalidUsername=null;
	

	public static HashMap<String, String> hash = new HashMap<String, String>();


	public void setLoginUsername(String email1)
	{
		GlobalVariables.email=email1.trim();
	}
	public void setLoginPassword(String password1)
	{
		GlobalVariables.password=password1.trim();
	}

	public String getLoginUsername()
	{
		return GlobalVariables.email.trim();
	}
	public String getLoginPassword()
	{
		return GlobalVariables.password.trim();
	}

	public void setTestEmail(String testemail)
	{
		GlobalVariables.testEmail=testemail.trim();
	}

	public String getTestEmail()
	{
		return GlobalVariables.testEmail.trim();
	}

	public void setTestUsername(String testusername)
	{
		GlobalVariables.testUsername=testusername.trim();
	}

	public String getTestUsername()
	{
		return GlobalVariables.testUsername.trim();
	}

	public void setInvalidUsername(String invalidusername)
	{
		GlobalVariables.invalidUsername=invalidusername.trim();
	}

	public String getInvalidUsername()
	{
		return GlobalVariables.invalidUsername.trim();
	}


	public GlobalVariables() {
		VerificationFlag = true;
	}

	public void setCurrentTestcase(String test) {
		currentTestcase = test;
	}

	public String getCurrentTestcase() {
		return currentTestcase;
	}

	public void setEnvironment(String env) {
		Environment = env;
	}

	public String getEnvironment() {
		return Environment;
	}
	public void setRelativePath(String path) {
		RelativePath = path;
	}

	public String getRelativePath() {
		return RelativePath;
	}

	public void setSummaryReportFile(String summaryfile) {

		SummaryReportFile = summaryfile;
	}

	public String getSummaryReportFile() {

		return SummaryReportFile;
	}

	public void setTestCaseReportFile(String testcasefile) {
		TestCaseReportFile = testcasefile;
	}

	public String getTestCaseReportFile() {
		return TestCaseReportFile;
	}

	public void setResultFolderPath(String path) {
		ResultPath = path;
	}

	public String getResultFolderPath() {
		return ResultPath;
	}

	public void setVerificationFlag(boolean flag) {
		VerificationFlag = flag;
	}

	public boolean getVerificationFlag() {
		return VerificationFlag;
	}

	public void setBrowser(String br) {
		browser = br;
	}

	public String getBrowser() {
		return browser;
	}
	public void setGridMode(String grid) {
		gridMode = grid;
	}

	public String getGridMode() {
		return gridMode;
	}
	public void setHubAddress(String hub)
	{
		hubAddress = hub;
	}
	public String getHubAddress()
	{
		return hubAddress;
	}
	public void setinputExcelFilePath(String path) {
		inputExcelFilePath = path;
	}

	public String getinputExcelFilePath() {
		return inputExcelFilePath;
	}

	public void setinputexcelFileName(String path) {
		inputexcelFileName = path;
	}

	public String getinputexcelFileName() {
		return inputexcelFileName;
	}

	public void setinputExcelSheetName(String sheet) {
		inputExcelSheetName = sheet;
	}

	public String getinputExcelSheetName() {
		return inputExcelSheetName;
	}

	public void setoutputExcelFilePath(String path) {
		outputExcelFilePath = path;
	}

	public String getoutputExcelFilePath() {
		return outputExcelFilePath;
	}

	public void setoutputExcelFileName(String fileName) {
		outputExcelFileName = fileName;
	}

	public String getoutputExcelFileName() {
		return outputExcelFileName;
	}
	public void setTestSuitePath(String path) {
		TestSuitePath = path;
	}

	public String getTestSuitePath() {
		return TestSuitePath;
	}
	
	
}
