package baseClasses;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.relevantcodes.extentreports.LogStatus;

public class TestListenerAdapter extends SeleniumUtils implements IInvokedMethodListener, ITestListener{
	
		
	private static String getTestMethodName(ITestResult iTestResult) {
				        return iTestResult.getMethod().getConstructorOrMethod().getName();
		    }
		   
		    //Before starting all tests, below method runs.
		    public void onStart(ITestContext iTestContext) {
		
		    }
		 
		    //
		    public void onFinish(ITestContext iTestContext) {
			
		        ExtentTestManager.endTest();
		        ExtentManager.getReporter().flush();
		    }
		 
		  public void onTestStart(ITestResult iTestResult) {
					    	//Start operation for extentreports in Extent Test Manager
		    	
		        DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmm");
		        String Filename = iTestResult.getName()+ "-" + df.format(new Date());
//		        System.out.println("Name of the Extent report for this Execution is : "+Filename);
		        //String ScenarioName = iTestResult.getMethod().getMethodName();
		        //String ScenarioName = iTestResult.getTestContext().getCurrentXmlTest().getParameter("ScenarioName");
		        String ScenarioName = iTestResult.getTestContext().getCurrentXmlTest().getName();
//		        System.out.println("Executing the Scenario "+ScenarioName);
		        ExtentTestManager.startTest(ScenarioName,"");
		    }
		 
		    public void onTestSuccess(ITestResult iTestResult) {
			
		    	//String ScenarioName = iTestResult.getMethod().getMethodName();
		    	//String ScenarioName = iTestResult.getTestContext().getCurrentXmlTest().getParameter("ScenarioName");
		    	String ScenarioName = iTestResult.getTestContext().getCurrentXmlTest().getName();
		    	//System.out.println("Scenario " +  ScenarioName + " got executed successfully");
		    	ExtentTestManager.getTest().log(LogStatus.INFO, ScenarioName+" : Scenario has been completed");
		    	ExtentTestManager.endTest();
		    	ExtentManager.getReporter().flush();
		    }
		 
		    public void onTestFailure(ITestResult iTestResult) {
		
		    	//String ScenarioName = iTestResult.getMethod().getMethodName();
		    	//String ScenarioName = iTestResult.getTestContext().getCurrentXmlTest().getParameter("ScenarioName");
		    	String ScenarioName = iTestResult.getTestContext().getCurrentXmlTest().getName();
		    	String Err_Msg = iTestResult.getThrowable().getMessage();
		    	System.out.println("Scenario " +  ScenarioName + " got failed, with the exception "+Err_Msg);
		    	
		    	try {
					String screenShot =SeleniumUtils.Capturefullscreenshot();
					ExtentTestManager.getTest().log(LogStatus.FAIL,ScenarioName +" : with the exception "+Err_Msg+ExtentTestManager.getTest().addBase64ScreenShot(screenShot));
					ExtentTestManager.endTest();
					ExtentManager.getReporter().flush();
				} catch (IOException e) {
					ExtentTestManager.endTest();
					ExtentManager.getReporter().flush();
				}
		    }
		 
		    public void onTestError(ITestResult iTestResult) {
				    	
		    	//String ScenarioName = iTestResult.getMethod().getMethodName();
		    	//String ScenarioName = iTestResult.getTestContext().getCurrentXmlTest().getParameter("ScenarioName");
		    	String ScenarioName = iTestResult.getTestContext().getCurrentXmlTest().getName();
		    	System.out.println("Error while Executing the scenario " +  ScenarioName + " ,Please verify");
		    	String Err_Msg = iTestResult.getThrowable().getMessage();
		        //Extentreports log operation for passed tests.
		    	//ExtentTestManager.getTest().log(LogStatus.ERROR, "Error while Executing the Scenario" + ScenarioName + " ,Please Verify");     
		    	try {		    		
					String screenShot =SeleniumUtils.Capturefullscreenshot();
					ExtentTestManager.getTest().log(LogStatus.ERROR,ScenarioName +" : with the exception "+Err_Msg+ExtentTestManager.getTest().addBase64ScreenShot(screenShot));
					SeleniumUtils.WEB_DRIVER_THREAD_LOCAL.get().close();
					ExtentTestManager.endTest();
					ExtentManager.getReporter().flush();
				} catch (IOException e) {
					ExtentTestManager.endTest();
					ExtentManager.getReporter().flush();
				}
		    }

		    public void onTestSkipped(ITestResult iTestResult) {
					    	
		    	//String ScenarioName = iTestResult.getMethod().getMethodName();
		    	//String ScenarioName = iTestResult.getTestContext().getCurrentXmlTest().getParameter("ScenarioName");
		    	String ScenarioName = iTestResult.getTestContext().getCurrentXmlTest().getName();
//		    	System.out.println("Scenario "+  ScenarioName + " got Skipped from execution");
		        ExtentTestManager.getTest().log(LogStatus.SKIP, "Scenario "+  ScenarioName + " got Skipped from execution");
		        ExtentTestManager.endTest();
		        ExtentManager.getReporter().flush();
		        
		    }
		 
		    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
					    	
		    	//String ScenarioName = iTestResult.getMethod().getMethodName();
		    	//String ScenarioName = iTestResult.getTestContext().getCurrentXmlTest().getParameter("ScenarioName");
		    	String ScenarioName = iTestResult.getTestContext().getCurrentXmlTest().getName();
		    	System.out.println("Scenario " +ScenarioName+" failed but it is in defined success ratio ");
		    	ExtentTestManager.getTest().log(LogStatus.INFO, "Scenario "+ScenarioName+" failed but it is in defined success ratio ");
		        ExtentTestManager.endTest();
		        ExtentManager.getReporter().flush();
		    }

			@Override
			public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
				// TODO Auto-generated method stub
				
			}
}
