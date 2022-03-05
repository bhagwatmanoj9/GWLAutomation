import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import baseClasses.DataMiner;
import baseClasses.GlobalVariables;
import baseClasses.ReadExcelFile;
import baseClasses.ReadingAndWritingTextFile;
import baseClasses.Report;
import baseClasses.Utilities;

class MainDriver 
{
	private static String testsuite=null;
	
	private static String RunBrowser=null;
	private static String Port=null;
	private static String ThreadCount=null;
	private static DocumentBuilderFactory dbFactory=null;
	private static DocumentBuilder dBuilder=null;
	private static Document doc=null;
	private static GlobalVariables g;
	private static String testScenario = null;	
	private static Utilities testSuite = new Utilities();
	private static ReadExcelFile readexcel = new ReadExcelFile();
	private static  ReadingAndWritingTextFile readText=new ReadingAndWritingTextFile();
	
	public static String sheet;
	
	public static String botId;
	public static String testData;
	public static String environment;
	public static String executionType;
	public static String testCases;
	public static String[] testCasesArray;
	public static String automationSuite;
	public static String gridMode;
	public static String BrowserStack;
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		MainDriver m = new MainDriver();
		Properties prop = new Properties();
		//Set Relative path and summary file in GlobalVaribles Class for Further Use
		String path = new File(".").getCanonicalPath();
		g = new GlobalVariables();
		g.setRelativePath(path);

/*	
		botId = System.getProperty("BOTID").trim();
		testData = System.getProperty("TESTDATA").trim();
		environment = System.getProperty("ENVIRONMENT").trim();
		executionType = System.getProperty("EXECUTIONTYPE").trim();
		testCases = System.getProperty("TESTCASES").trim();
		testCasesArray = testCases.split(",");
		int TotalTestCases = testCasesArray.length;
		automationSuite = System.getProperty("AUTOMATIONSUITE").trim(); 
		BrowserStack = System.getProperty("BrowserStack").trim();
*/		
		//String path = new File(".").getCanonicalPath();
		//g.setRelativePath(path);
		
		InputStream inputA = new FileInputStream(g.getRelativePath()+"/configuration.properties");
		 prop.load(inputA);
		
		String dataFileName = prop.getProperty("TestDataSheet");
        File path2 = new File("./TestData/"+dataFileName);
        String testDataFile2 = path2.toString();
        
        String Environment = prop.getProperty("Environment");
		String System = DataMiner.fngetconfigvalue(testDataFile2, Environment, "System");
		if(System.equalsIgnoreCase("Remote")){
			executionType = "parallel";
		}else{
			executionType = "sequential";
		}
		
		
		botId = "8";
		testData = "OAT_testdata.xlsx";
		environment = "Stage";
		//executionType = "parallel";
		//testCases = "End To End Booking flow without editing basket and Tip,End To End Booking flow with adding a Tip but user is not editing basket";
		testCases = 
				
				
				"Launch the GWR mobile web browser,Verify the History and return button after login,verify table number,verify New message page,verify click on review button"
				;

		testCasesArray = testCases.split(",");
		int TotalTestCases = testCasesArray.length;
		automationSuite = "Servy Automation";
		//BrowserStack = "No";
		
		try (OutputStream output = new FileOutputStream(g.getRelativePath()+"/configuration.properties")) {

            //Properties prop = new Properties();

            // set the properties value
            prop.setProperty("BotId", botId);
            prop.setProperty("TestDataSheet", testData);
            prop.setProperty("Environment", environment);
            prop.setProperty("TotalTestCases", String.valueOf(TotalTestCases));
            prop.setProperty("AuomationSuite", automationSuite);
            prop.setProperty("ExecutionType", executionType);
            //prop.setProperty("BrowserStack", BrowserStack);
            // save properties to project root folder
            prop.store(output, null);
            //System.out.println(prop);

        } catch (IOException io) {
            io.printStackTrace();
        }
		
		
		switch (automationSuite) {
		case "Servy Automation":
			sheet="GWR_Automation";
			break;		
		default: 
			Report.LogInfo("AutomationSuite", "Unsupported automation suite selected", "INFO");
			break;
		}
		
		if(executionType.equalsIgnoreCase("Parallel"))
		{
			gridMode= "ON";
		}else
		{
			gridMode= "OFF";
		}
		
		
		if(gridMode.trim().equalsIgnoreCase("ON"))
		{
			//Execute hub
			try
			{
				RunScript(g.getRelativePath()+"//Grid","Hub.bat");
				RunBrowser = testSuite.getValue("Browsers", "firefox");
				Port = testSuite.getValue("ports", "5555");

				String brwArray[] =RunBrowser.split(",");
				String portArray[] =Port.split(",");

				//g.setPortNumber(portArray);

				for(int i=0;i<portArray.length;i++)
				{
					/*if(automationSuite.equalsIgnoreCase("NMTS Number Management")){
						brwArray[i] = "IE";
					}*/
					m.prepareNode(portArray[i],brwArray[i]);
					RunScript(g.getRelativePath()+"//Grid","node.bat");
					Thread.sleep(3000);
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			//JOptionPane.showMessageDialog(null, "Run Node with different port", "", 0);
		} else
		{			
				RunBrowser = testSuite.getValue("Browsers", "firefox");
				String brwArray[] =RunBrowser.split(",");
				RunBrowser = brwArray[0];			
		}

		testsuite = testSuite.getValue("ExecutionSuite", "").trim();
						
		int totalrow = readexcel.getTotalRowsExcel(g.getRelativePath() +"//Resources//"+testsuite+".xls", sheet);

		//delete all the testcases added in testng xml file
		m.checkndDeleteAllnodes(g.getRelativePath() +"//Resources//testng.xml");
		
		String ColName= "Test_Scenario";
		
		 File path1 = new File("./TestData/"+testData);
	     String testDataFile = path1.toString();
		
		for(int tcCount=0;tcCount<testCasesArray.length;tcCount++)
		{
			String expectedTC = testCasesArray[tcCount];
		
			for(int rowC=1;rowC<totalrow;rowC++)
			{

				testScenario = m.getTestScenarioName(g.getRelativePath()+"//Resources//"+testsuite+".xls",sheet,rowC,ColName,expectedTC);
				//testScenario1 = m.getTestScenarioName(g.getRelativePath()+"//Resources//"+testsuite+".xls",sheet,rowC,ColName,expectedTC);

				if(testScenario!=null)
				{
					int totalcol = readexcel.getTotalColumnExcel(g.getRelativePath()+"//Resources//"+testsuite+".xls", sheet);		
					String[] testDetails = getTestcaseDetails(g.getRelativePath()+"//Resources//"+testsuite+".xls", sheet, totalcol, rowC);
					ArrayList<String> iSubScriptArry = DataMiner.fnGetiSubScriptValue(testDataFile, testDetails[2], testDetails[3], "Scenario_Name", testDetails[1]);
					
					for(int sCount=0;sCount<iSubScriptArry.size();sCount++)
					{
						//Pass the testcase details to create testng xml function
						m.CreateTestngXml(g.getRelativePath() +"//Resources//testng.xml",testDetails,sheet,executionType,tcCount,iSubScriptArry.get(sCount));
					}
					
					break;
				}
			}
		}
		
		//for(int i=0;i<RunBrowser.split(",").length;i++)
		//{
		
			//Running the Runscript batch file to Run the Maven
			try {
				RunScript(g.getRelativePath(),"RunScript.bat");
				Thread.sleep(5000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//}		
	
	}

	public void checkndDeleteAllnodes(String xmlPath)
	{
		try
		{
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(xmlPath);

			NodeList list = doc.getElementsByTagName("suite");
			for (int i = 0; i <  list.getLength(); i++) 
			{  
				// Get Node  
				Node node =  list.item(i);  
				System.out.println(node.getNodeName());
				
				NodeList childList = node.getChildNodes();  
				// Look through all the children  
				for (int x = 0; x < childList.getLength(); x++) 
				{  
					Node child = (Node) childList.item(x); 
					if(child.getNodeName().equalsIgnoreCase("test") || child.getNodeName().equalsIgnoreCase("parameter") || child.getNodeName().equalsIgnoreCase("classes"))
					{
						child.getParentNode().removeChild(child);  
					}
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult streamResult =  new StreamResult(new File(xmlPath));
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, streamResult);


		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

	}

	public void CreateTestngXml(String xmlPath, String[] testDetails, String sheet, String executionType, int tcCount, String iSubScript)
	{
		try
		{
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(xmlPath);
			
			ThreadCount = testSuite.getValue("ThreadCount", "1");

			//checkndDeleteAllnodes(xmlPath);
			Node suite = doc.getFirstChild();
			NamedNodeMap attr = suite.getAttributes();	       
			
			if(tcCount==0)	
			{			
				Node nameAttr = attr.getNamedItem("name");
				nameAttr.setTextContent(sheet);								
				Node parallelAttr = attr.getNamedItem("parallel");
				Node threadCount = attr.getNamedItem("thread-count");
				if(executionType.trim().equalsIgnoreCase("parallel"))
				{
					parallelAttr.setTextContent("tests");
					threadCount.setTextContent(ThreadCount);
				}else
				{
					parallelAttr.setTextContent("false");					
				}
							
			}
			
			Element testElm = doc.createElement("test");
			testElm.setAttribute("name", testDetails[1]+"_"+iSubScript);
			suite.appendChild(testElm);
				
			Element parameterElm1 = doc.createElement("parameter");
			parameterElm1.setAttribute("name", "dataSheet");
			parameterElm1.setAttribute("value", testDetails[2]);
			testElm.appendChild(parameterElm1);
			
			Element parameterElm2 = doc.createElement("parameter");
			parameterElm2.setAttribute("name", "ScenarioName");
			parameterElm2.setAttribute("value", testDetails[1]);
			testElm.appendChild(parameterElm2);
			
			Element parameterElm3 = doc.createElement("parameter");
			parameterElm3.setAttribute("name", "scriptNo");
			parameterElm3.setAttribute("value", testDetails[3]);
			testElm.appendChild(parameterElm3);
			
			Element parameterElm4 = doc.createElement("parameter");
			parameterElm4.setAttribute("name", "dataSetNo");
			parameterElm4.setAttribute("value", iSubScript);
			testElm.appendChild(parameterElm4);
			
			Element classesElm = doc.createElement("classes");
			testElm.appendChild(classesElm);
			
			Element classElm = doc.createElement("class");
			classElm.setAttribute("name", testDetails[5]+"."+testDetails[6]);
			classesElm.appendChild(classElm);
		
			doc.normalize();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult streamResult =  new StreamResult(new File(xmlPath));
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, streamResult);

		}
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}

	}


	private static String[] getTestcaseDetails(String xlFilePath, String sheetName,int coloumn, int rowC)
	{
		String temp[]=new String[coloumn];

		for(int i=0;i<coloumn;i++)
		{
			temp[i] = readexcel.getDataFromCell(xlFilePath, sheetName, rowC, i);	
		}

		return temp;
	}

	public void prepareNode(String port,String browsertype)
	{
		BufferedReader br= null;
		BufferedWriter wr = null;
		String line=null;
		String nodeFile=g.getRelativePath()+"//Grid//Nodetemplate//nodeTemplate.bat";
		String tempFile=g.getRelativePath()+"//Grid//node.bat";
		try
		{
			br = new BufferedReader(new FileReader(nodeFile));
			wr = new BufferedWriter(new FileWriter(tempFile));
			
			while ((line = br.readLine()) != null)
			{
				if(line.indexOf("PORTNUM")>0)
				{
					line=line.replace("PORTNUM", port);
				}
				if(line.indexOf("BROWSER")>0)
				{
					String browser=null;
					if(browsertype.trim().equalsIgnoreCase("FIREFOX"))
					{
						browser = "browserName=\"firefox\",version=ANY,platform=WINDOWS,maxInstances=1 -maxSession 1";

					}else if(browsertype.trim().equalsIgnoreCase("EDGE"))
					{
						browser = "browserName=\"edge\",version=ANY,platform=WINDOWS,maxInstances=1 -maxSession 1";
					}else if(browsertype.trim().equalsIgnoreCase("CHROME"))
					{
						browser = "browserName=\"chrome\",version=ANY,platform=WINDOWS,maxInstances=1 -maxSession 1";
					}
					else if(browsertype.trim().equalsIgnoreCase("IE"))
					{
						browser = "browserName=\"iexplore\",version=ANY,platform=WINDOWS,maxInstances=1 -maxSession 1";
					}

					line =line.replace("BROWSER", browser);
				}
				if(line.indexOf("DRIVER")>0)
				{
					String driver=null;
					if(browsertype.trim().equalsIgnoreCase("FIREFOX"))
					{
						driver = "-Dwebdriver.gecko.driver=\"%~dp0geckodriver.exe\"";

					}else if(browsertype.trim().equalsIgnoreCase("EDGE"))
					{
						driver = "-Dwebdriver.edge.driver=\"%~dp0msedgedriver.exe\"";
					}else if(browsertype.trim().equalsIgnoreCase("CHROME"))
					{
						driver = "-Dwebdriver.chrome.driver=\"%~dp0chromedriver.exe\"";
					}
					else if(browsertype.trim().equalsIgnoreCase("IE"))
					{
						driver = "-Dwebdriver.chrome.driver=\"%~dp0IEDriverServer.exe\"";
					}

					line =line.replace("DRIVER", driver);
				}
								
				wr.write(line);
			}
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		finally
		{
			try {
				br.close();
				wr.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}


	public static void RunScript(String path,String batchFile) throws Exception
	{

		try
		{
			//Runtime.getRuntime().exec("cmd /c start "+g.getRelativePath()+"//RunScript.bat");
			Process p =Runtime.getRuntime().exec("cmd /c start "+path+"//"+batchFile+"");
			p.waitFor();		

		}
		catch (IOException e) 
		{
			System.out.println("Error While Executing RunScript batch file");
			System.out.println(e.getMessage());
		}

	}

	//====================================================================================================
	// FunctionName    	: getTestcaseName
	// Description     	: Function to get the Scenario Name which need to execute based on execute flag
	// Input Parameter 	: testSuitepath- Testsuite Excel file path
	//					: sheetName- Sheet Name
	//					: row - row number
	//					: Col - Column name
	// Return Value    	: TestScenario Name
	//====================================================================================================	

	public String getTestScenarioName(String testSuitepath, String sheet , int row, String Col, String expectedTC)
	{


		String flag = readexcel.getDataFromExcel(testSuitepath, sheet, Col, row);
		if(flag.trim().equalsIgnoreCase((expectedTC)))
		{
			return (readexcel.getDataFromExcel(testSuitepath, sheet, Col, row));
		}
		else
		{
			return null;	
		}
	}
}