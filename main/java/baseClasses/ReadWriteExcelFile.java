package baseClasses;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ReadWriteExcelFile
{

	//private static GlobalVariables g = new GlobalVariables();
	//private static Utilities util = new Utilities();

	//====================================================================================================
	// TestCaseName    	: getSheetFromExcel
	// Description     	: Function to get the worksheet from Excel
	// Input Parameter 	: xlFilePath - Input excel file path
	//					: sheetName - Sheet name of excel worksheet
	// Return Value    	: Sheet - worksheet object
	//====================================================================================================	
	public List<Object> getSheetFromExcel(String xlFilePath, String sheetName)
	{

		 Sheet workSheet = null;
		 FormulaEvaluator evaluator = null;
		try{
			
			//Create an object of File class to open xlsx file

		    File file =    new File(xlFilePath);

		    if (file.exists() == true) 
		    {
		    	//Create an object of FileInputStream class to read excel file

		    	FileInputStream inputStream = new FileInputStream(file);

		    	Workbook workbook = null;

		    	//Find the file extension by splitting file name in substring  and getting only extension name

		    	String fileExtensionName = xlFilePath.substring(xlFilePath.indexOf("."));

		    	//Check condition if the file is xlsx file

		    	if(fileExtensionName.equals(".xlsx")){

		    		//If it is xlsx file then create object of XSSFWorkbook class

		    		workbook = new XSSFWorkbook(inputStream);
		    	}

		    	//Check condition if the file is xls file

		    	else if(fileExtensionName.equals(".xls")){

		    		//If it is xls file then create object of HSSFWorkbook class

		    		workbook = new HSSFWorkbook(inputStream);
		    	}

		    	//Read sheet inside the workbook by its name
		    	//workbook.setForceFormulaRecalculation(true);
		    	
		    	evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		    	workSheet = workbook.getSheet(sheetName);
		    }

		}catch (Exception e) 
		{

			//Report.LogInfo("ExceptionInExcelReading", "Error in getting worksheet from excelsheet : " +e.getMessage(), "FAIL");

		}
		return Arrays.asList(workSheet, evaluator);
	}
	
	//====================================================================================================
		// TestCaseName    	: getWorkbookFromExcel
		// Description     	: Function to get the workbook from Excel
		// Input Parameter 	: xlFilePath - Input excel file path
		//					
		// Return Value    	: Workbook - Workbook object
		//====================================================================================================	
		public Workbook getWorkbookFromExcel(String xlFilePath)
		{
			 Workbook workbook = null;
			
			try{
				
				//Create an object of File class to open xlsx file

			    File file =    new File(xlFilePath);

			    //Create an object of FileInputStream class to read excel file

			    FileInputStream inputStream = new FileInputStream(file);

			       //Find the file extension by splitting file name in substring  and getting only extension name

			    String fileExtensionName = xlFilePath.substring(xlFilePath.indexOf("."));

			    //Check condition if the file is xlsx file

			    if(fileExtensionName.equals(".xlsx")){

			    //If it is xlsx file then create object of XSSFWorkbook class

			    	workbook = new XSSFWorkbook(inputStream);

			    }

			    //Check condition if the file is xls file

			    else if(fileExtensionName.equals(".xls")){

			        //If it is xls file then create object of HSSFWorkbook class

			    	workbook = new HSSFWorkbook(inputStream);

			    }

			    
			}catch (Exception e) 
			{

				//Report.LogInfo("ExceptionInExcelReading", "Error in getWorkbookFromExcel: " +e.getMessage(), "FAIL");

			}
			return(workbook);
		}
	
	//====================================================================================================
	// FunctionName    	: getDataFromCell
	// Description     	: Function to get the data from cell based on row number and column number
	// Input Parameter 	: xlFilePath- Excel file path
	//					: sheetName- Sheet Name
	//					: row - Row Number
	//					: coloumn - Column number
	// Return Value    	: Data
	//====================================================================================================	

	public String getDataFromCell(String xlFilePath,String sheetName,int rowNo,int colNo) 
	{
		String cellData=null;
		Row row=null;
		Cell cell=null;
		try
		{
			List<Object> getSheetReturn = getSheetFromExcel(xlFilePath, sheetName); 
			Sheet sheet = (Sheet) getSheetReturn.get(0);
			
			FormulaEvaluator evaluator = (FormulaEvaluator) getSheetReturn.get(1);
			
			row = sheet.getRow(rowNo);
			if(row==null)
			{
				
			}else
			{
				cell = row.getCell(colNo);
				if(cell==null)
				{
					
				}else
				{
					evaluator.evaluateFormulaCell(cell);
					cellData = cell.getStringCellValue();
				}
				
			}			

		}catch(Exception e)
		{
			//Report.LogInfo("Exception", "Exception in getDataFromCell "+e.getMessage(), "FAIL");
		}
		return cellData;
	}


	
	//====================================================================================================
	// FunctionName    	: getTestcaseName
	// Description     	: Function to get the Scenario Name which need to execute based on execute flag
	// Input Parameter 	: testSuitepath- Testsuite Excel file path
	//					: sheetName- Sheet Name
	//					: row - row number
	//					: col - Column number
	// Return Value    	: TestScenario Name
	//====================================================================================================	

	public String getTestScenarioName(String testSuitepath, String sheet , int row, int runModeCol, int scenarioCol)
	{


		String flag = getDataFromCell(testSuitepath, sheet, row, runModeCol);
		if(flag.trim().toUpperCase().equals("YES"))
		{
			return (getDataFromCell(testSuitepath, sheet, row, scenarioCol));
		}
		else
		{
			return null;	
		}
	}
	
	//====================================================================================================
		// FunctionName    	: getTotalRowsExcel
		// Description     	: Function to get total rows from excel
		// Input Parameter 	: testSuitepath- Testsuite Excel file path
		//					: sheetName- Sheet Name
		// Return Value    	: Row Count
		//====================================================================================================	

		public int getTotalRowsExcel(String testSuitepath, String sheet)
		{

			int rowCount = 0;
			try
			{
				List<Object> getSheetReturn = getSheetFromExcel(testSuitepath, sheet); 
				Sheet wsheet = (Sheet) getSheetReturn.get(0);
				//Sheet wsheet = getSheetFromExcel(testSuitepath, sheet);
				if(wsheet==null)
				{
					
				}else
				{
					rowCount = wsheet.getLastRowNum()-wsheet.getFirstRowNum();
				}

			}catch(Exception e)
			{
				//Report.LogInfo("Exception", "Exception in getTotalRowsExcel "+e.getMessage(), "FAIL");
			}
			return rowCount;
		}
	
		//====================================================================================================
		// FunctionName    	: getTotalColumnExcel
		// Description     	: Function to get total columns from excel
		// Input Parameter 	: testSuitepath- Testsuite Excel file path
		//					: sheetName- Sheet Name
		//					: rowNum - Current row number
		// Return Value    	: Column Count
		//====================================================================================================	

		public int getTotalColumnExcel(String testSuitepath, String sheet, int rowNum)
		{

			int colCount = 0;
			try
			{
				List<Object> getSheetReturn = getSheetFromExcel(testSuitepath, sheet); 
				Sheet wsheet = (Sheet) getSheetReturn.get(0);
				
				Row row = wsheet.getRow(rowNum);
				colCount = row.getLastCellNum();

			}catch(Exception e)
			{
				//Report.LogInfo("Exception", "Exception in getTotalColumnExcel "+e.getMessage(), "FAIL");
			}
				return colCount;
		}
		
		//====================================================================================================
		// FunctionName    	: writeDataToCell
		// Description     	: Function to write data to cell based on row number and column number
		// Input Parameter 	: xlFilePath- Excel file path
		//					: sheetName- Sheet Name
		//					: row - Row Number
		//					: coloumn - Column number
		// Return Value    	: Data
		//====================================================================================================	

		public void writeDataToCell(String xlFilePath,String sheetName,int rowNo,int colNo, String data) 
		{
			Row row=null;
			Cell cell=null;
			try
			{
				List<Object> getSheetReturn = getSheetFromExcel(xlFilePath, sheetName); 
				Sheet sheet = (Sheet) getSheetReturn.get(0);
				
				
				row = sheet.getRow(rowNo);
				if(row==null)
				{
					
				}else
				{
					cell = row.getCell(colNo);
					if(cell==null)
					{
						
					}else
					{
						cell.setCellValue(data);
					}
					
				}
				
				// Write the output to a file
		        FileOutputStream fileOut = new FileOutputStream(xlFilePath);
		        sheet.getWorkbook().write(fileOut);
		        fileOut.close();

			}catch(Exception e)
			{
				//Report.LogInfo("Exception", "Exception in getDataFromCell "+e.getMessage(), "FAIL");
			}
			
		}

}