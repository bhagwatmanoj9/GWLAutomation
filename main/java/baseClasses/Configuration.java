package baseClasses;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class Configuration {

	public static String URL_INFO = null;
	// public static String PASSWORD= properties.getProperty("password");
	private static GlobalVariables glb = new GlobalVariables();
	static Properties prop = new Properties();
	static GlobalVariables g = new GlobalVariables();

	public static String GWR_URL;
	
	
	
	public static void setUrlInfoDataSet(HashMap<String, Object> basicURLinfo) throws IOException, InterruptedException {
		
		try (InputStream input = new FileInputStream(g.getRelativePath()+"/configuration.properties"))
		{

	        // load a properties file
	        prop.load(input);               

	    }
		catch (IOException ex) 
		{
	        ex.printStackTrace();
	    }
		String dataFileName = prop.getProperty("TestDataSheet");
		File path = new File("./TestData/"+dataFileName);
		String testDataFile = path.toString();
		
		 String Environment = prop.getProperty("Environment");
		 
		 GWR_URL = DataMiner.fngetconfigvalue(testDataFile, Environment, "GWR_URL");
		 

	}

	// ====================================================================================================
	// FunctionName : getURL
	// Description : Function to get all the URL information
	// Input Parameter : None
	// Return Value :
	// ====================================================================================================
	public static String getURL()
	{
		Properties prop = new Properties();
		
		try (InputStream input = new FileInputStream(glb.getRelativePath()+"/configuration.properties")) {

            // load a properties file
            prop.load(input);               

        } catch (IOException ex) {
            ex.printStackTrace();
        }
	    	    
		//String Environment = util.getValue("Environment", "RFS").trim();
	    String Environment = prop.getProperty("Environment");
	    //System.out.println("Environment is: "+Environment);
		glb.setEnvironment(Environment);
		if(Environment.equalsIgnoreCase("Stage"))
		{
			URL_INFO = "Stage";

		}else if(Environment.equalsIgnoreCase("Test1"))
		{
			URL_INFO = "Test1";

		}else if(Environment.equalsIgnoreCase("UAT"))
		{
			URL_INFO = "UAT";

		}
		return URL_INFO;
	}
}