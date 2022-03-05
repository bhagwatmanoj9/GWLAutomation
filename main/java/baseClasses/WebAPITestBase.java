package baseClasses;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.json.JSONObject;
import org.json.XML;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.yaml.snakeyaml.Yaml;

import com.relevantcodes.extentreports.LogStatus;

import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

public class WebAPITestBase {
	
	protected HashMap<String,String> testData=new HashMap<String,String>();
	XmlPath path;
	public static Response response;
	protected static GlobalVariables g = new GlobalVariables();
	public Connection con = null;
	public static Statement st = null;
	public static RestHighLevelClient client = null;
	private static Utilities suiteConfig = new Utilities();
	public static String NBORDERID;
	public String host;
	public String port;
	public String elasticIp;
	public int elasticPort;
	public static String elasticIndex;
	public static int TotalCases;
	public String AutomationSuite;
	protected static String timestamp=null;
	Properties prop = new Properties();
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> setBasicInfomationForTestData(String fileName) 
	{

		HashMap<String, Object> basicInfoDataset=null;
		try
		{
			FileInputStream input = new FileInputStream(new File("./Resources/URL_information.yml"));
			Yaml yaml = new Yaml();
			basicInfoDataset = ((HashMap<String, Object>) ((HashMap<String, Object>) yaml.load(input)).get(fileName));

		}catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
			JOptionPane.showMessageDialog(null, "Error in YML file loading,Check the syntax");
		}
		return basicInfoDataset;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> getUrlInfoDataset(HashMap<String, Object> ymlurlDataset, String urlTagName) throws FileNotFoundException 
	{
		return ((HashMap<String, Object>) ymlurlDataset.get(urlTagName));
	}
	
	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() throws Exception
	{
		String path = new File(".").getCanonicalPath();
		g.setRelativePath(path);
		
		InputStream inputA = new FileInputStream(g.getRelativePath()+"/configuration.properties");
		 prop.load(inputA); 
		System.setProperty("logback.configurationFile",g.getRelativePath()+"/logback.xml");
		AutomationSuite = prop.getProperty("AuomationSuite");
		if(AutomationSuite.equals("NH B2B Rest API"))
		{
			g.setBrowser("IE");
		}else{
		
		g.setBrowser(suiteConfig.getValue("Browsers", "firefox").trim().split(",")[0]);

		}
		
		g.setGridMode(suiteConfig.getValue("GridMode", "off").trim());
		g.setTestSuitePath(path+"//Resources//TestSuite.xlsx");
		
		timestamp = "RegressionSuite_"+suiteConfig.getCurrentDatenTime("MM-dd-yyyy")+"_"+suiteConfig.getCurrentDatenTime("hh-mm-ss_a");

		String resultPath = path+"//CTAFResults//"+timestamp;
		String ScreenshotsPath = resultPath+"//Screenshots";

		g.setResultFolderPath(resultPath);
		g.setTestSuitePath(path+"//Resources//TestSuite.xls");

		new File(resultPath).mkdirs();
		new File(ScreenshotsPath).mkdirs();

		String SummaryReportfile = resultPath+"\\Index.html";

		Report.createSummaryReportHeader(SummaryReportfile);
		

		try{
			InputStream input1 = new FileInputStream(g.getRelativePath()+"/configuration.properties");
			InputStream input2 = new FileInputStream(g.getRelativePath()+"/PortalDBConfig.properties");

            // load a properties file
            prop.load(input1); 
            prop.load(input2);

        } catch (IOException ex) {
            ex.printStackTrace();
        }			
		System.setProperty("logback.configurationFile",g.getRelativePath()+"/logback.xml");
		NBORDERID = prop.getProperty("BotId");
		host = prop.getProperty("Host");
		port = prop.getProperty("Port");
		elasticIp = prop.getProperty("elasticIp");
		elasticPort = Integer.parseInt(prop.getProperty("elasticPort"));
		elasticIndex = prop.getProperty("elasticIndex");
		TotalCases = Integer.parseInt(prop.getProperty("TotalTestCases"));
			
		Class.forName("com.mysql.jdbc.Driver"); 
		con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/colt", "root", "admin");
		st = con.createStatement();
		
		client = new RestHighLevelClient(RestClient.builder(new HttpHost(elasticIp, elasticPort, "http")));
}

	@Parameters({ "ScenarioName" })
	@BeforeMethod()
	public void setUp(String ScenarioName) throws Exception
	{
		st.execute("UPDATE tc_execution set STATUS=\"Inprogress\" WHERE req_id="+ NBORDERID + " AND tc_name='"+ScenarioName+"';");
		st.execute("UPDATE req_summary SET Total="+TotalCases+" WHERE req_id="+ NBORDERID+";");
			
		//Return an Object of HashMap with All values stored from URL_information yml file
		HashMap<String, Object> basicInfoDataset = setBasicInfomationForTestData("url_information");

		//Setting all the Required public variable with values from YML file for Specific Environment
		Configuration.setUrlInfoDataSet(getUrlInfoDataset(basicInfoDataset,Configuration.getURL()));

		//file.setEmailFlag(false);
		//String Automessage=email.getExecutionStartMessage();
		//email.sendMessageWithAttachment("CTAF Automation Regression Test Run Started",Automessage,"",false);
		
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() throws Exception
	{
		/**************************Zip the Output generated HTML Files********************************/
		ZipDirectory zip = new ZipDirectory();
		File directoryToZip = new File(g.getResultFolderPath());
		List<File> fileList = new ArrayList<File>();
		System.out.println("---Getting references to all files in: " + directoryToZip.getCanonicalPath());
		zip.getAllFiles(directoryToZip, fileList);
		System.out.println("---Creating zip file");

		String zipOut=g.getResultFolderPath() +"//"+directoryToZip.getName() + ".zip";
		System.out.println(zipOut);
		zip.writeZipFile(directoryToZip, fileList,zipOut);
		System.out.println("---Done");

		//text.setEmailFlag(true);

		//file.setEmailFlag(true);
		zipOut=g.getResultFolderPath() +"//"+directoryToZip.getName() + ".zip";
		Report.createSummaryReportFooter();
		Report.BotSummary();
		
		if (isProcessRunning("cmd.exe"))
        {
			System.out.println("Inside CMD KILL");
			Runtime.getRuntime().exec("taskkill /T /F /IM cmd.exe");
        }	
		
		/**************************Send the mail with zip output HTML Files********************************/
		//email.sendMessageWithAttachment("CTAF Automated Regression Test Run Completed",file.readEntireFile(g.getResultFolderPath() +"//Index.html"),zipOut,true );
	}
	
	public Boolean verifyStatusCode(Response response, int statusCode){
		Boolean bResults;
		ExtentTestManager.getTest().log(LogStatus.INFO,"Response status - "+ response.getStatusLine());
		Report.LogInfo("Verify Status Code","Response status - "+ response.getStatusLine(), "INFO");
		if (response.getStatusCode() == statusCode){
			ExtentTestManager.getTest().log(LogStatus.PASS,"Actual Response Code matches the Expected Response Code.");
			Report.LogInfo("Verify Status Code","Actual Response Code matches the Expected Response Code.", "PASS");

			bResults = true;
		}else{
			ExtentTestManager.getTest().log(LogStatus.FAIL,"Expected code "+statusCode+" but returned "+ response.getStatusCode());
			Report.LogInfo("Verify Status Code","Expected code "+statusCode+" but returned "+ response.getStatusCode(), "FAIL");

			bResults = false;
		}
		return bResults;
	}	
	
	public void compareActualWithExpected(Response response, String ExpectedStatus)
	{
		String ActualStatus;
		String[] TransArray;
		String TransactionId;
		String actualRes = response.getBody().asString();
		String prettyResponse = response.getBody().asPrettyString();
		
		if(response.getBody().asString() != null)
		{
			//path = new XmlPath(actualRes);
			path = new XmlPath(prettyResponse);
			
			ActualStatus = path.getString("status"); 
			TransArray = ActualStatus.split(ExpectedStatus.trim());
			TransactionId = TransArray[1];
			
			JSONObject json = XML.toJSONObject(actualRes);   
	        String jsonActual = json.toString(4);  
						
			if(ActualStatus.contains(ExpectedStatus))
			{
				ExtentTestManager.getTest().log(LogStatus.PASS,"Response Body is: "+ jsonActual);
				Report.LogInfo("compareActualWithExpected","Response Body is: "+ jsonActual, "PASS");

				//ExtentTestManager.getTest().log(LogStatus.PASS,"Transaction Id is: "+TransactionId);'
			}else
			{
				ExtentTestManager.getTest().log(LogStatus.FAIL,"<br />Response:<br />"+ jsonActual);
				Report.LogInfo("compareActualWithExpected","<br />Response:<br />"+ jsonActual, "FAIL");

			}
		}else
		{
			ExtentTestManager.getTest().log(LogStatus.FAIL,"Failed<br />Actual Response:<br />"+ actualRes);
			Report.LogInfo("compareActualWithExpected","Failed<br />Actual Response:<br />"+ actualRes, "FAIL");

		}
			
	}
	
	public Response get(Map<String, String> authhdrs, String endPoint, String inputDataType, String inputData){
		
		ExtentTestManager.getTest().log(LogStatus.INFO,"GET request on the API EndPoint : "+ endPoint);	
		Report.LogInfo("GET Request","GET request on the API EndPoint : "+ endPoint, "INFO");

		switch (inputDataType) {
			case "None":
				  response = given().urlEncodingEnabled(false).request().headers(authhdrs).get(endPoint).andReturn();
				break;
			case "URLEncoded":
				endPoint= endPoint+inputData;
				response = given().urlEncodingEnabled(false).request().headers(authhdrs).get(endPoint).andReturn();
				break;
			case "Payload":
				  response = given().urlEncodingEnabled(false).request().headers(authhdrs).body(inputData).get(endPoint).andReturn();
				break;
			default: 
				
				break;
			   }	
		return response;
	}
			
	public Response post(Map<String, String> authhdrs, String endPoint, String payLoad) throws IOException
	{
		ExtentTestManager.getTest().log(LogStatus.INFO,"POST request on the API EndPoint : "+ endPoint);
		Report.LogInfo("POST Request","POST request on the API EndPoint : "+ endPoint, "INFO");

		response = given().urlEncodingEnabled(false).request().headers(authhdrs).body(payLoad).when().post(endPoint);
		//System.out.println(response.getBody().asString());
		return response;			
	}	
	
	public Response delete(Map<String, String> authhdrs, String endPoint, String inputDataType, String inputData)
	{
		ExtentTestManager.getTest().log(LogStatus.INFO,"DELETE request on the API EndPoint : "+ endPoint);
		Report.LogInfo("DELETE Request","DELETE request on the API EndPoint : "+ endPoint, "INFO");

		switch (inputDataType) {
			    case "None":
				   response = given().urlEncodingEnabled(false).request().headers(authhdrs).delete(endPoint).andReturn();
				     break;
			    case "URLEncoded":
				      endPoint= endPoint+inputData;
				      response = given().urlEncodingEnabled(false).request().headers(authhdrs).delete(endPoint).andReturn();
				     break;
			    case "Payload":
				      response = given().urlEncodingEnabled(false).request().headers(authhdrs).body(inputData).delete(endPoint).andReturn();
				     break;
			    default: 
				  
				    break;
			       }  
		return response;
	}
	
	private static boolean isProcessRunning(String processName) throws IOException, InterruptedException
    {
        ProcessBuilder processBuilder = new ProcessBuilder("tasklist.exe");
        Process process = processBuilder.start();
        String tasksList = toString(process.getInputStream());

        return tasksList.contains(processName);
    }

    // http://stackoverflow.com/a/5445161/3764804
    private static String toString(InputStream inputStream)
    {
        Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
        String string = scanner.hasNext() ? scanner.next() : "";
        scanner.close();

        return string;
    }
	
}
