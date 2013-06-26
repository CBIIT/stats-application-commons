/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

/**
 * 
 */
package gov.nih.nci.caintegrator.application.download;

import gov.nih.nci.caintegrator.application.zip.ZipItem;
import gov.nih.nci.caintegrator.application.zip.ZipManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author sahnih
 *
 */
public class DownloadZipHelper {
	private static Logger logger = Logger.getLogger(DownloadZipHelper.class);

	//private static String ftpURL = "ftp://cbiocgemsftp.nci.nih.gov";

	public static ZipItem  fileToZip(String fileName, String zipFileName, String directoryInZip, String inputDirectory){
		ZipItem zipItem = null;
        /////Setup the Zip File
		String filePath = inputDirectory+File.separator+fileName;
	    File f = new File(filePath);
	    if (f.exists()) {
	    	zipItem = new DownloadZipItemImpl();
	        zipItem.setFileName(zipFileName);
	        zipItem.setFilePath(filePath);
	        zipItem.setDirectoryInZip(directoryInZip);
	        zipItem.setFileSize(f.length());

	    }
		return zipItem;
	}
	public static List<ZipItem> filesToZip(List<String> fileNames, String zipFileName, String directoryInZip,String inputDirectory){
		List<ZipItem> zipItems = new ArrayList<ZipItem>();
        /////Setup the Zip File
		for(String fileName:fileNames){
			String filePath = inputDirectory+File.separator+fileName;
		    File f = new File(filePath);
		    if (f.exists()) {
		    	ZipItem zipItem = fileToZip(fileName, zipFileName, directoryInZip,inputDirectory);
		    	if(zipItem != null){
		    		zipItems.add(zipItem);
		    	}
		    }
		}
		return zipItems;
	}
	public static List<String> zipFile(List<ZipItem> zipItemCollection, String zipFileName,String outputZipDirectory, boolean breakIntoMultipleFileIfLarge){
		if(zipItemCollection != null){
			// start zipping and wait for it to finish
			ZipManager zipper = new ZipManager();
			zipper.setItems(zipItemCollection);
	        zipper.setTarget(outputZipDirectory+File.separator+zipFileName);
	        zipper.setBreakIntoMultipleFileIfLarge(breakIntoMultipleFileIfLarge);
	        zipper.run();
	        return zipper.getListofZipFiles();
		}
		return null;
	}

	/**
	 * delete downloaded file
	 * @return true is delete is successful.
	 */
	public static boolean deleteFile(String fileName, String inputDirectory){
		// Now delete the old text file since it is no longer needed
		boolean success = false;
		File f = new File(inputDirectory+File.separator+fileName);
		
		success = f.delete();
		if (!success)
			logger.error("Error trying to delete file " + fileName);
		
		return success;
	}
	/**
	 * delete zipped file
	 * @return true is delete is successful.
	 */
	public static boolean deleteZipFile(String zipFileName,String outputZipDirectory){
		// Now delete the old zip file since it is no longer needed
		boolean success = false;
		File f = new File( outputZipDirectory+File.separator+zipFileName);
		
		success = f.delete();
		if (!success)
			logger.error("Error trying to delete file " + zipFileName);
		
		return success;
	}
	/**
	 * delete zipped file
	 * @return true is delete is successful.
	 */
	public static boolean checkIfFileExists(String file, long fileSize ,String inputDirectory){
		// Now delete the old zip file since it is no longer needed
		boolean success = false;
		//Verify Zip File Exsists before sending email
		File newFile = new File(inputDirectory+File.separator+file);
        if(newFile.exists()  && newFile.isFile() && newFile.length() == fileSize)
        {
        	success = true;
        }
        return success;
	}
	/**
	 * Returns a Java  file with the CaArrayFile name as a prefix
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static File createFile(String file,String inputDirectory) throws IOException {
		File newFile = new File(inputDirectory+File.separator+file);
		return newFile;
	}
}


