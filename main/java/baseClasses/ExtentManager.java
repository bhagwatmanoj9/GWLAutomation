package baseClasses;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.relevantcodes.extentreports.ExtentReports;


//OB: ExtentReports extent instance created here. That instance can be reachable by getReporter() method.

public class ExtentManager {

  private static ExtentReports extent;
  static Properties prop = new Properties();
  private static GlobalVariables glb = new GlobalVariables();

  public synchronized static ExtentReports getReporter(){
      
	  try (InputStream input = new FileInputStream(glb.getRelativePath()+"/configuration.properties")) {

          // load a properties file
          prop.load(input);               

      } catch (IOException ex) {
          ex.printStackTrace();
      }
	  
	  if(extent == null){
          //Set HTML reporting file location
          String workingDir = System.getProperty("user.dir");
          extent = new ExtentReports(workingDir+"\\ExtentReports\\"+prop.getProperty("BotId")+"\\ExtentReportResults.html", true);
          extent.loadConfig(new File(workingDir+"\\config-report.xml"));
          System.out.println("Bot ID is: "+prop.getProperty("BotId"));
          Report.logToDashboard("Bot ID is: "+prop.getProperty("BotId"));
      }
      return extent;
  }  
  
}
