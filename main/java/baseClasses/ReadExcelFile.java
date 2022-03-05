package baseClasses;

import java.io.File;

import com.relevantcodes.extentreports.LogStatus;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


public class ReadExcelFile
{


	private static GlobalVariables g = new GlobalVariables();
	private static Utilities util = new Utilities();

	//====================================================================================================
	// TestCaseName    	: getTestDataFromExcel
	// Description     	: Function to get the Data from Excel based on virtual table created in an Excel sheet
	// Input Parameter 	: xlFilePath - Input excel file path
	//					: sheetName - Sheet name of excel worksheet
	//					: tableName - Virtual table name in excel sheet
	// Return Value    	: String[][] - two dimensional array with all value within virtual table
	//====================================================================================================	
	public String[][] getTestDataFromExcel(String xlFilePath, String sheetName, String tableName)
	{

		String[][] tabArray=null;
		try{
			Workbook workbook = Workbook.getWorkbook(new File(xlFilePath));
			Sheet sheet = workbook.getSheet(sheetName);
			int startRow,startCol, endRow, endCol,ci,cj;
			Cell tableStart=sheet.findCell(tableName);
			startRow=tableStart.getRow();
			startCol=tableStart.getColumn();

			Cell tableEnd= sheet.findCell(tableName, startCol+1,startRow+1, 500, 64000,  false);                               

			endRow=tableEnd.getRow();
			endCol=tableEnd.getColumn();
			System.out.println("startRow="+startRow+", endRow="+endRow+", " +
					"startCol="+startCol+", endCol="+endCol);
			tabArray=new String[endRow-startRow-1][endCol-startCol-1];
			ci=0;

			for (int i=startRow+1;i<endRow;i++,ci++)
			{
				cj=0;
				for (int j=startCol+1;j<endCol;j++,cj++)
				{
					tabArray[ci][cj]=sheet.getCell(j,i).getContents();
				}
			}

		}catch (Exception e) 
		{

			Report.LogInfo("ExceptionInExcelReading", "Error in reading Table in excelsheet : " +e.getMessage(), "FAIL");

		}
		return(tabArray);
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

	public String getDataFromCell(String xlFilePath,String sheetName,int row,int col) 
	{
		Cell tableStart=null;
		try
		{
			Workbook workbook = Workbook.getWorkbook(new File(xlFilePath));
			Sheet sheet = workbook.getSheet(sheetName);
			tableStart=sheet.getCell(col,row);

		}catch(Exception e)
		{
			Report.LogInfo("Exception", "Exception in getDataFromCell "+e.getMessage(), "FAIL");
		}
		return tableStart.getContents();
	}

	//====================================================================================================
	// FunctionName    	: getDataRowNumExcel
	// Description     	: Function to get the Row Number for particular value in excel
	// Input Parameter 	: xlFilePath- Excel file path
	//					: sheetName- Sheet Name
	//					: Value - Data for which row number is require
	// Return Value    	: Rownumber
	//====================================================================================================	

	public int getDataRowNumExcel(String xlFilePath, String sheetName,String Value) 
	{
		Cell tableStart=null;
		try
		{
			Workbook workbook = Workbook.getWorkbook(new File(xlFilePath));
			Sheet sheet = workbook.getSheet(sheetName);
			tableStart=sheet.findCell(Value);

		}catch(Exception e)
		{
			Report.LogInfo("Exception", "Exception in getDataRowNumExcel "+e.getMessage(), "FAIL");
			System.out.println(e.getMessage());
		}
		return tableStart.getRow();
	}

	//====================================================================================================
	// FunctionName    	: getDataColumnNumExcel
	// Description     	: Function to get the Column Number for particular value in excel
	// Input Parameter 	: xlFilePath- Excel file path
	//					: sheetName- Sheet Name
	//					: Value - Data for which row number is require
	// Return Value    	: Column Number
	//====================================================================================================	

	public int getDataColumnNumExcel(String xlFilePath, String sheetName,String Value) 
	{
		Cell tableStart=null;
		try
		{
			Workbook workbook = Workbook.getWorkbook(new File(xlFilePath));
			Sheet sheet = workbook.getSheet(sheetName);
			tableStart=sheet.findCell(Value);

		}catch(Exception e)
		{
			Report.LogInfo("Exception", "Exception in getDataRowNumExcel "+e.getMessage(), "FAIL");
			System.out.println(e.getMessage());
		}
		return tableStart.getColumn();
	}

	//====================================================================================================
	// FunctionName    	: getExcelColumnValue
	// Description     	: Function to get the value based on column value passed, it will give the next row value in front of column value present in excel
	// Input Parameter 	: xlFilePath- Excel file path
	//					: sheetName- Sheet Name
	//					: column - Column Name in excel
	// Return Value    	: Value of Column
	//====================================================================================================
	public String getExcelColumnValue(String xlFilePath, String sheetName,String coloumn) 
	{
		Cell cell=null;
		String data=null;
		try
		{
			Workbook workbook = Workbook.getWorkbook(new File(xlFilePath));
			Sheet sheet = workbook.getSheet(sheetName);
			cell=sheet.findCell(coloumn);
			cell=sheet.getCell(cell.getColumn(),(cell.getRow()+1));
			data = cell.getContents();
		}catch(Exception e)
		{
			data = "";
			Report.LogInfo("Exception", "Exception in getExcelColumnValue "+e.getMessage() , "FAIL");
			ExtentTestManager.getTest().log(LogStatus.ERROR, "Exception in getExcelColumnValue "+e.getMessage());
		}
		//return cell.getContents();
		return data;
	}
	//====================================================================================================
	// FunctionName    	: getExcelRowValue
	// Description     	: Function to get the value of based on Row value passed, it will give next column value in front of row value present in excel
	// Input Parameter 	: xlFilePath- Excel file path
	//					: sheetName- Sheet Name
	//					: row - unique row value
	// Return Value    	: Value in front of that unique row value
	//====================================================================================================
	public String getExcelRowValue(String xlFilePath, String sheetName,String row) 
	{
		Cell cell=null;
		try
		{
			Workbook workbook = Workbook.getWorkbook(new File(xlFilePath));
			Sheet sheet = workbook.getSheet(sheetName);
			cell=sheet.findCell(row);
			cell=sheet.getCell(cell.getColumn()+1,(cell.getRow()));
		}catch(Exception e)
		{

			System.out.println(e.getMessage());
			Report.LogInfo("Exception", "Exception in getExcelRowValue "+e.getMessage() , "FAIL");
		}
		return cell.getContents();
	}

	//====================================================================================================
	// FunctionName    	: getDataFromExcel
	// Description     	: Function to get the Data from excel based on column name and row number
	// Input Parameter 	: xlFilePath- Excel file path
	//					: sheetName- Sheet Name
	//					: column - Column Name in excel
	//					: row- row number for which value need
	// Return Value    	: Value of Column for that particular row
	//====================================================================================================
	public String getDataFromExcel(String xlFilePath, String sheetName,String Column, int row) 
	{
		Cell tableStart=null;
		try
		{
			Workbook workbook = Workbook.getWorkbook(new File(xlFilePath));
			Sheet sheet = workbook.getSheet(sheetName);
			tableStart=sheet.findCell(Column);
			tableStart=sheet.getCell(tableStart.getColumn(),row);

		}catch(Exception e)
		{
			Report.LogInfo("Exception", "Exception in getExcelColumnValue "+e.getMessage() , "FAIL");
			System.out.println(e.getMessage());
		}
		return tableStart.getContents();
	}
	//====================================================================================================
	// FunctionName    	: getTotalRowsExcel
	// Description     	: Function to get the total number of rows present in an excel
	// Input Parameter 	: xlFilePath- Excel file path
	//					: sheetName- Sheet Name
	// Return Value    	: Total row numbers
	//====================================================================================================
	public int getTotalRowsExcel(String xlFilePath, String sheetName) 
	{
		Sheet sheet = null;
		try
		{
			Workbook workbook = Workbook.getWorkbook(new File(xlFilePath));
			sheet = workbook.getSheet(sheetName);

		}catch(Exception e)
		{
			Report.LogInfo("Exception", "Exception in getTotalRowsExcel "+e.getMessage() , "FAIL");
			System.out.println(e.getMessage());
		}
		return sheet.getRows();
	}

	//====================================================================================================
	// FunctionName    	: getTotalColumnExcel
	// Description     	: Function to get the total number of Columns present in an excel
	// Input Parameter 	: xlFilePath- Excel file path
	//					: sheetName- Sheet Name
	// Return Value    	: Total row numbers
	//====================================================================================================
	public int getTotalColumnExcel(String xlFilePath, String sheetName) 
	{
		Sheet sheet = null;
		try
		{
			Workbook workbook = Workbook.getWorkbook(new File(xlFilePath));
			sheet = workbook.getSheet(sheetName);

		}catch(Exception e)
		{
		}
		return sheet.getColumns();
	}
	//====================================================================================================
	// FunctionName    	: getTotalFieldsPresentInaRow
	// Description     	: Function to get the total number of Fields present in InputExcel(testdata) Header Row, for the use of excel creation
	// Input Parameter 	: xlFilePath- Excel file path
	//					: sheetName- Sheet Name
	//					: row- row number
	// Return Value    	: Total Field Count
	//====================================================================================================	
	public int getTotalHeaderFieldPresentInExcel(String xlFilePath,String sheetName,int row)
	{
		Sheet sheet = null;
		String celVal = null;
		int FieldCounter = -1;
		int Columnindex =1;
		try
		{
			Workbook workbook = Workbook.getWorkbook(new File(xlFilePath));
			sheet = workbook.getSheet(sheetName);
			do
			{

				celVal=sheet.getCell(Columnindex,row).getContents();
				Columnindex = Columnindex +1;
				FieldCounter = FieldCounter  +1;

			}while(!celVal.equals(""));

		}catch(Exception e)
		{
			Report.LogInfo("Exception", "Exception in getTotalFieldsPresentInaRow "+e.getMessage(), "FAIL");
			System.out.println(e.getMessage());
		}

		return FieldCounter;
	}

	//====================================================================================================
	// FunctionName    	: getTotalColumnonSpecificRowInExcel
	// Description     	: Function to get the total number of Fields present in excel sheet on specific row
	// Input Parameter 	: xlFilePath- Excel file path
	//					: sheetName- Sheet Name
	//					: row- row number
	// Return Value    	: Total Field Count
	//====================================================================================================	
	public int getTotalColumnonSpecificRowInExcel(String xlFilePath,String sheetName,int row)
	{
		Sheet sheet = null;
		String celVal = null;
		int FieldCounter = -1;
		int Columnindex =0;
		try
		{
			Workbook workbook = Workbook.getWorkbook(new File(xlFilePath));
			sheet = workbook.getSheet(sheetName);
			do
			{

				celVal=sheet.getCell(Columnindex,row).getContents();
				Columnindex = Columnindex +1;
				FieldCounter = FieldCounter  +1;

			}while(!celVal.equals(""));


		}catch(Exception e)
		{
			return FieldCounter;
			//Report.LogInfo("Exception", "Exception in getTotalColumnonSpecificRowInExcel "+e.getMessage(), "FAIL");
			//System.out.println(e.getMessage());
		}

		return FieldCounter;
	}
	//====================================================================================================
	// FunctionName    	: getArrayIndex
	// Description     	: Function to get the index return where the data is present in excel, which will be using to create output excel
	// Input Parameter 	: Data- Two dimensional array with all virtual table data from input excel
	//					: data- Data value for which index need to fetch
	// Return Value    	: index
	//====================================================================================================	
	public int getArrayIndex(String[][] sData,String data)
	{
		String temp="";
		boolean flag=false;
		int i=0;
		for(i=0;i<sData.length;i++)
		{
			for(int j=0;j<sData[i].length;j++)
			{
				temp = sData[i][j];
				if(temp.equals(""))
				{
					i++;
					j=-1;
					continue;
				}
				if(temp.equalsIgnoreCase(data))
				{
					flag=true;
					break;
				}
			}

			if(flag)
			{
				break;
			}
		}
		return i;
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

	public String getTestScenarioName(String testSuitepath, String sheet , int row, String Col)
	{


		String flag = getDataFromExcel(testSuitepath, sheet, Col, row);
		if(flag.trim().toUpperCase().equals("YES"))
		{
			return (getDataFromExcel(testSuitepath, sheet, "Scenarios", row));
		}
		else
		{
			return null;	
		}
	}
	//====================================================================================================
	// FunctionName    	: setValueinExcelCell
	// Description     	: Function to set the Value in cell
	// Return Value    	: Rownumber
	//====================================================================================================	

	public void setValueinExcelCell(String xlFilePath, String sheetName,int rowNum,int colNum,String Value) 
	{
		Workbook workbook=null;
		WritableWorkbook copy = null;

		try
		{
			workbook = Workbook.getWorkbook(new File(xlFilePath));
			Thread.sleep(4000);
			copy = Workbook.createWorkbook(new File(g.getRelativePath()+"//TestData//temp.xls"), workbook);

			WritableSheet sheet2 = copy.getSheet(0); 
			WritableCell cell = sheet2.getWritableCell(colNum, rowNum); 

			if (cell.getType() == CellType.NUMBER) 
			{ 

				Number num = (Number) cell;
				num.setValue(Integer.valueOf(Value));
			}
			if (cell.getType() == CellType.LABEL) 
			{ 

				Label l = (Label) cell; 
				l.setString(Value); 
			}

			copy.write(); 
			copy.close();
			workbook.close();


			File file = new File(g.getRelativePath()+"//TestData//temp.xls");
			File file2 = new File(xlFilePath);
			System.out.println(file2.delete());
			Thread.sleep(2000);
			System.out.println(file.renameTo(file2));


		}catch(Exception e)
		{
			Report.LogInfo("Exception", "Exception in setValueinExcelCell "+e.getMessage(), "FAIL");
			ExtentTestManager.getTest().log(LogStatus.FAIL, "Exception in setValueinExcelCell "+e.getMessage());
			System.out.println(e.getMessage());
			try {
				copy.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			workbook.close();
		}
	}	
}