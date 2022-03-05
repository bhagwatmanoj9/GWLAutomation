package baseClasses;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.swing.JOptionPane;

import org.testng.annotations.DataProvider;
public class AutomationDataProvider {
	static ReadExcelFile xlsData = new ReadExcelFile();
	static GlobalVariables glb = new GlobalVariables();

	static String testData = glb.getRelativePath() + "//Resources//TestSuite.xls";
	@DataProvider(name = "ReadingExcelSheet")
	public static Object[][] createInputData(Method method) throws IOException
	{

		String test = method.getName();
		Object[][] retObjArr =null;

		if (test.equals("testLogin") ) 
		{
			retObjArr =xlsData.getTestDataFromExcel(testData,"testLogin",test);
		}
		else if(test.equals("testLogin1"))
		{
			retObjArr = xlsData.getTestDataFromExcel(testData,"testLogin1",test);
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Test data is not added for this test "+test+"case in Dataprovider");
		}
		return (retObjArr);
	}

}
