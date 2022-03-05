package baseClasses;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataMiner {
	
		
	@SuppressWarnings("deprecation")
	public static String fngetcolvalue(String file_name,String sheet_name, String iScript, String iSubScript, String col_name )throws IOException, InterruptedException {
	/*----------------------------------------------------------------------
	Method Name : fngetcolvalue
	Purpose     : to get the column value of the required column name
	Input       : file_name,sheet_name, iScript, iSubScript, col_name
	Output      : sVal3
	 ----------------------------------------------------------------------*/	
	String sResult = null;
	
		for (int i = 1; i < 10; i++) {
			try {
				FileInputStream file_stream = new FileInputStream(file_name);
				@SuppressWarnings("resource")
				XSSFWorkbook work_book = new XSSFWorkbook(file_stream);
				XSSFSheet work_sheet = work_book.getSheet(sheet_name);
				//int row_count = work_sheet.getlastCellNum();
				XSSFRow row = work_sheet.getRow(0);
				int last_row = work_sheet.getLastRowNum();
				int last_Col = row.getLastCellNum();
				//XSSFCell cell_val =  work_sheet.getRow(0).getCell(1);
				int isScript = 0,iter = 0,isSubScript = 0,act_row = 0,act_col = 0;
				String sVal3 = "";
				for (iter = 0; iter<=last_Col-1; iter++) {
					if (row.getCell(iter).getStringCellValue().trim().equals("iScript"))
						isScript = iter;
					if (row.getCell(iter).getStringCellValue().trim().equals("Data_Set_No"))
						isSubScript = iter;
					if (row.getCell(iter).getStringCellValue().trim().equals(col_name))
						act_col = iter;
				}
				
				for (int iRow = 1; iRow <= last_row; iRow++) {
					   row = work_sheet.getRow(iRow);
					   Cell cell = row.getCell(isScript);
					   cell.setCellType(CellType.STRING);
					   String sVal1 = row.getCell(isScript).getStringCellValue();
					   String sVal2 = row.getCell(isSubScript).getStringCellValue();
					   if (sVal1.equalsIgnoreCase(iScript) && sVal2.equalsIgnoreCase(iSubScript))
					   {
						   act_row = iRow;
						   row = work_sheet.getRow(act_row);
						   try {
							   sVal3 = row.getCell(act_col).getStringCellValue();
							   sResult = sVal3;
							   file_stream.close();
							   break;
						   } catch (Exception e) {
							   file_stream.close();
								return "";
							}
					   }
				   }
				  
			} catch (Exception e) {
				Thread.sleep(2500);
				continue;
			}
		}
		return sResult;
	}
	
	
	public static void fnsetcolvalue(String file_name,String sheet_name, String iScript, String iSubScript, String col_name, String col_value )throws IOException, InterruptedException
	/*----------------------------------------------------------------------
	Method Name : fnsetcolvalue
	Purpose     : to enter the value to required cell
	Input       : file_name,sheet_name, iScript, iSubScript, col_name, col_value
	Output      : sVal3
	 ----------------------------------------------------------------------*/	
	{
		for (int i=0; i<10; i++) {
			try {
				FileInputStream file_stream = new FileInputStream(file_name);
				@SuppressWarnings("resource")
				XSSFWorkbook work_book = new XSSFWorkbook(file_stream);
				XSSFSheet work_sheet = work_book.getSheet(sheet_name);
				//int row_count = work_sheet.getlastCellNum();
				XSSFRow row = work_sheet.getRow(0);
				int last_row = work_sheet.getLastRowNum();
				int last_Col = row.getLastCellNum();
				//XSSFCell cell_val =  work_sheet.getRow(0).getCell(1);
				int isScript = 0,iter = 0,isSubScript = 0,act_row = 0,act_col = 0;
				for (iter = 0; iter<=last_Col-1; iter++) {
					if (row.getCell(iter).getStringCellValue().trim().equals("iScript"))
						isScript = iter;
					if (row.getCell(iter).getStringCellValue().trim().equals("Data_Set_No"))
						isSubScript = iter;
					if (row.getCell(iter).getStringCellValue().trim().equals(col_name))
						act_col = iter;
				}
			   for (int iRow = 1; iRow <= last_row; iRow++) {
				   row = work_sheet.getRow(iRow);
				   Cell cell = row.getCell(isScript);
				   cell.setCellType(CellType.STRING);
				   String sVal1 = row.getCell(isScript).getStringCellValue();
				   String sVal2 = row.getCell(isSubScript).getStringCellValue();
				   if (sVal1.equalsIgnoreCase(iScript) && sVal2.equalsIgnoreCase(iSubScript))
				   {
					   act_row = iRow;
					   //System.out.println("actual row value "+act_row);
					   row = work_sheet.getRow(act_row);
					   cell = row.getCell(act_col);
					   cell.setCellType(CellType.STRING);
					   cell.setCellValue(col_value);
					   FileOutputStream fos = new FileOutputStream(file_name);
					   work_book.write(fos);
					   fos.close();
					   file_stream.close();
					   break;
				   }
			   }
			} catch (Exception e) {
				Thread.sleep(2500);
				continue;
			}
		}
	}
	
	public static int fngetrowcount(String file_name,String sheet_name ) throws IOException
	/*----------------------------------------------------------------------
	Method Name : fngetrowcount
	Purpose     : to get used row count of a particular excel sheet
	Input       : file_name,sheet_name
	Output      : last_row
	 ----------------------------------------------------------------------*/	
	{
		FileInputStream file_stream = new FileInputStream(file_name);
		@SuppressWarnings("resource")
		XSSFWorkbook work_book = new XSSFWorkbook(file_stream);
		XSSFSheet work_sheet = work_book.getSheet(sheet_name);
		int last_row = work_sheet.getLastRowNum();
		file_stream.close();
		return last_row;
	}
	
	public static String fngetScriptval(String file_name,String sheet_name, int iRow, String rSript)throws IOException
	/*----------------------------------------------------------------------
	Method Name : fngetScriptval
	Purpose     : to get column Value of iScript on a current iteration
	Input       : file_name,sheet_name, iRow, rSript
	Output      : sVal
	 ----------------------------------------------------------------------*/	
	{
		FileInputStream file_stream = new FileInputStream(file_name);
		@SuppressWarnings("resource")
		XSSFWorkbook work_book = new XSSFWorkbook(file_stream);
		XSSFSheet work_sheet = work_book.getSheet(sheet_name);
		XSSFRow row = work_sheet.getRow(0);
		int last_Col = row.getLastCellNum();
		int isScript = 0,iter = 0; String sVal = null;
		for (iter = 0; iter<=last_Col-1; iter++)
		{
			if (row.getCell(iter).getStringCellValue().trim().equals(rSript))
				isScript = iter;
		}
		   row = work_sheet.getRow(iRow);
		   try {
			   sVal = row.getCell(isScript).getStringCellValue();

			   file_stream.close();
		   } catch (Exception e) {
			   sVal = "";
			   file_stream.close();
		   }
		return sVal;
	}
	
	@SuppressWarnings("deprecation")
	public static String fngetconfigvalue(String file_name, String Environment, String col_name)throws IOException, InterruptedException {
	/*----------------------------------------------------------------------
	Method Name : fngetconfigvalue
	Purpose     : to get the credentials of the particular environment
	Input       : String file_name, String col_name
	Output      : sVal3
	 ----------------------------------------------------------------------*/
	
	String sResult = null;
	
	for (int i = 0; i<10; i++) {
		try {
			FileInputStream file_stream = new FileInputStream(file_name);
			@SuppressWarnings("resource")
			XSSFWorkbook work_book = new XSSFWorkbook(file_stream);
			XSSFSheet work_sheet = work_book.getSheet("Config_Sheet");
			XSSFRow row = work_sheet.getRow(0);
			int last_row = work_sheet.getLastRowNum();
			int last_Col = row.getLastCellNum();
			int isScript = 0,iter = 0,isSubScript = 0,act_row = 0,act_col = 0,env_col = 0;
			String sVal3 = "";
			for (iter = 0; iter<=last_Col-1; iter++) {
				if (row.getCell(iter).getStringCellValue().trim().equals("Environment")) {
					env_col = iter;
				} 
				if (row.getCell(iter).getStringCellValue().trim().equals(col_name)) {
					act_col = iter;
				} 
				
			}
			
			for (int iRow = 1; iRow <= last_row; iRow++) {
				   row = work_sheet.getRow(iRow);
				   Cell cell = row.getCell(isScript);
				   cell.setCellType(CellType.STRING);
				   String sVal1 = row.getCell(env_col).getStringCellValue();
				   if (sVal1.equalsIgnoreCase(Environment)) {
					   act_row = iRow;
					   //System.out.println("actual row value "+act_row);
					   row = work_sheet.getRow(act_row);
					   try {
						   sVal3 = row.getCell(act_col).getStringCellValue().trim();
						   sResult = sVal3;
						   file_stream.close();
						   break;
					   } catch (Exception e) {
						   file_stream.close();
							return "";
						}
				   }
			   }
			  return sVal3;
		} catch (Exception e) {
			Thread.sleep(2500);
			continue;
		}
	}
		return sResult;
	}
		
	@SuppressWarnings("deprecation")
	public static ArrayList<String> fnGetiSubScriptValue(String file_name,String sheet_name, String iScript, String col_name, String scenario_name)throws IOException, InterruptedException {
	/*----------------------------------------------------------------------
	Method Name : fngetcolvalue
	Purpose     : to get the column value of the required column name
	Input       : file_name,sheet_name, iScript, iSubScript, col_name
	Output      : sVal3
	 ----------------------------------------------------------------------*/	
		
	String sVal1 = "";
	String sVal2 = "";
	String sVal3 = "";
		
	ArrayList<String> temp = new ArrayList<String>();
	
		FileInputStream file_stream = new FileInputStream(file_name);
		@SuppressWarnings("resource")
		XSSFWorkbook work_book = new XSSFWorkbook(file_stream);
		XSSFSheet work_sheet = work_book.getSheet(sheet_name);
		
		//int row_count = work_sheet.getlastCellNum();
		XSSFRow row = work_sheet.getRow(0);
		int last_row = work_sheet.getLastRowNum();
		int last_Col = row.getLastCellNum();
		//XSSFCell cell_val =  work_sheet.getRow(0).getCell(1);
		int isScript = 0,iter = 0,isSubScript = 0,act_row = 0,act_col = 0;
				
		for (iter = 0; iter<=last_Col-1; iter++) {
			if (row.getCell(iter).getStringCellValue().trim().equals("iScript"))
				isScript = iter;
			if (row.getCell(iter).getStringCellValue().trim().equals("Data_Set_No"))
						isSubScript = iter;
			if (row.getCell(iter).getStringCellValue().trim().equals(col_name))
						act_col = iter;
		}
				
		for (int iRow = 1; iRow <= last_row; iRow++) {
		   row = work_sheet.getRow(iRow);
		   if (row!=null)
		   {
		   Cell cell = row.getCell(isScript);
		   if (cell!=null)
		   {
		   cell.setCellType(CellType.STRING);
		   sVal1 = row.getCell(isScript).getStringCellValue();
		   sVal3 = row.getCell(act_col).getStringCellValue();
		   if (sVal1!=null && sVal3!=null)
		   {
		   if (sVal1.equalsIgnoreCase(iScript) && sVal3.equalsIgnoreCase(scenario_name))
		   {
				   act_row = iRow;
				   row = work_sheet.getRow(act_row);
				try {
						sVal2 = row.getCell(isSubScript).getStringCellValue();
						temp.add(sVal2);
						
					 } catch (Exception e) {
						   file_stream.close();							
					 }
				}				   
		   }
		   }
		   }
					 
			if(iRow == last_row)
			{
			  file_stream.close();
			  break;
			}
		}
			return temp;
	}
	


}
