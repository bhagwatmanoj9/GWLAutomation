package baseClasses;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipDirectory
{

	private static GlobalVariables glb = new GlobalVariables();

	public long getFileSize(File file)
	{
		long filesize = file.length();
		long filesizeInKB = filesize / 1024;
		System.out.println("Size of File is: "+ filesizeInKB + " KB");
		return filesizeInKB;
	}

	public  void getAllFiles(File dir, List<File> fileList) {
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				//checking the size of files, if it's less than 500Kb then only add in ZIP
				if(getFileSize(file) < 500)
				{
					fileList.add(file);
				}
				if (file.isDirectory()) {
					System.out.println("directory:" + file.getCanonicalPath());
					getAllFiles(file, fileList);
				} else {
					System.out.println("     file:" + file.getCanonicalPath());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public  void writeZipFile(File directoryToZip, List<File> fileList, String zipPath) 
	{

		try {
			FileOutputStream fos = new FileOutputStream(glb.getResultFolderPath() +"//"+directoryToZip.getName() + ".zip");
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (File file : fileList) 
			{
				if (!file.isDirectory())
				{ // we only zip files, not directories
					addToZip(directoryToZip, file, zos);
				}
			}

			zos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public  void addToZip(File directoryToZip, File file, ZipOutputStream zos) throws FileNotFoundException,IOException 
	{

		FileInputStream fis = new FileInputStream(file);
		String zipFilePath = file.getCanonicalPath().substring(directoryToZip.getCanonicalPath().length() + 1,
				file.getCanonicalPath().length());
		System.out.println("Writing '" + zipFilePath + "' to zip file");
		ZipEntry zipEntry = new ZipEntry(zipFilePath);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) 
		{
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}

}