package baseClasses;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Utilities
{

	private static Calendar calendar=null;
	static GlobalVariables glb = new GlobalVariables();
	private static Random random = new Random((new Date()).getTime());

	public String getValue(String key, String def)
	{
		BufferedReader br = null;

		GlobalVariables g = new GlobalVariables();
		String path =g.getRelativePath()+"\\TestSuite.ini";

		String line = ""; 
		String val = "";
		Boolean found = false;
		try
		{
			br = new BufferedReader(new FileReader(path));
			String regex = "=";
			
			while ((line = br.readLine())!=null)
			{
				if (line.trim().length()>0)
				{
					String[] pairs = line.split(regex);
					if (pairs[0].trim().equals(key))
					{
						val = pairs[1];
						found = true;
					}
				}
			}
		}
		catch (Exception ex)
		{
			System.out.println("Error While Reading TestSuite.ini file");
			System.out.println(ex.getMessage());
		}
		finally
		{
			try
			{
				br.close();
			}
			catch (Exception ex)
			{
				System.out.println("Error While Close the TestSuite read object");
			}
			if (!found)
			{
				val = def;
			}
		}

		return val;
	}


	public String getCurrentDatenTime(String format)
	{
		Calendar cal = Calendar.getInstance();
		calendar = cal;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(cal.getTime());
	}

	public String getNextDatenTime(String format)
	{
		Calendar cal = Calendar.getInstance();
		calendar = cal;
		cal.add(Calendar.DATE, 1);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(cal.getTime());
	}

	public boolean isInteger(String data)
	{
		Pattern p = Pattern.compile("[0-9]*");
		Matcher m = p.matcher(data);
		boolean result = m.matches();
		return result;
	}
	//====================================================================================================
	// FunctionName    	: GenerateRandom
	// Description     	: Function to Generate Random Number
	// Input Parameter 	: range
	// Return Value    	: BigInteger random number
	//====================================================================================================
	public BigInteger GenerateRandom(BigInteger range)
	{
		Random rand = new Random();
		BigInteger result = new BigInteger(range.bitLength(), rand);
		while( result.compareTo(range) >= 0 ) {
			result = new BigInteger(range.bitLength(), rand);
		}
		return result;
	}

	//====================================================================================================
	// FunctionName    	: unZipFile
	// Description     	: Function to Unzip the Zip File and Store at required folder
	// Input Parameter 	: None
	// Return Value    	: 
	//====================================================================================================	
	public String unZipFile(String zipPath,String unzipFilePath)
	{
		Enumeration entries;
		ZipFile zipFile;
		String unzipFile=null; 
		try 
		{
			zipFile = new ZipFile(zipPath);

			entries = zipFile.entries();

			while(entries.hasMoreElements())
			{
				ZipEntry entry = (ZipEntry)entries.nextElement();

				if(entry.isDirectory())
				{
					// Assume directories are stored parents first then children.
					System.err.println("Extracting directory: " + entry.getName());
					// This is not robust, just for demonstration purposes.
					(new File(entry.getName())).mkdir();
					continue;
				}

				System.err.println("Extracting file: " + entry.getName());

				InputStream in=zipFile.getInputStream(entry);
				unzipFile= unzipFilePath+entry.getName();
				OutputStream out=new BufferedOutputStream(new FileOutputStream(unzipFile));

				byte[] buffer = new byte[1024];
				int len;

				while((len = in.read(buffer)) >= 0)
					out.write(buffer, 0, len);

				in.close();
				out.close();

				//copyInputStream(zipFile.getInputStream(entry),new BufferedOutputStream(new FileOutputStream(entry.getName())));
			}
			zipFile.close();
		}
		catch (IOException ioe) 
		{
			System.err.println("Unhandled exception:");
			ioe.printStackTrace();
			return null;
		}
		return unzipFile;
	}
	
	public String getYesterDayDatenTime(String format)
	{
		Calendar cal = Calendar.getInstance();
		calendar = cal;
		
		cal.add(Calendar.DATE, -1);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(cal.getTime());
	}


}