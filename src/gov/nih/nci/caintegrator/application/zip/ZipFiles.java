/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

/*
 * $Revision: 1.2 $
 * $Date: 2007-06-15 18:36:06 $
 * $Author: rossok $
 * $Name: not supported by cvs2svn $
 * $Id: ZipFiles.java,v 1.2 2007-06-15 18:36:06 rossok Exp $
 * Created on Aug 23, 2005
 *
 *
 *
 */
package gov.nih.nci.caintegrator.application.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

/**
 * @author Prashant Shah - NCICB/SAIC
 * 
 *
 *
 */
public class ZipFiles extends AbstractFileZipper {

    public static final int BUFFER = 204800;
    public static final int NO_COMPRESSION = 0;
    public static final int DEFAULT_COMPRESSION = 1;
    public static final int MID_COMPRESSION = 4;
    public static final int MAX_COMPRESSION = 9;
    private static Logger logger = Logger.getLogger(ZipFiles.class);

    // Stream to read data being zipped
    private BufferedInputStream origin;
    
    // Stream to write to the zip file
    private ZipOutputStream out;
    
    // Buffer
    private byte data[];
 
    
    // List of file names that have been zipped
    // There can be several if a very large amount of data is zipped
    private List<String> fileList = new ArrayList<String>();
    
    // Zip file being created at this point in time
    private String outputFileName = null;

    /**
     * 
     */
    public ZipFiles() {

        origin = null;
        out = null;
    }

    // Set output Zipped file in init() method
    public void startNewFile(String fileName, int sequenceNumber) throws Exception {

        try {
        
        	if(sequenceNumber == 1)
        	 {
        	   // This is the first zip file for this data. 
        	   // Just use the name passed in
        	   this.outputFileName = fileName;
        	 }
        	else
        	 {
        		// This is not the first zip file for this data
        		// Append a number (e.g. _1 _2) to the file name
        		int dotIndex = fileName.lastIndexOf('.');
        		String beforeDot = fileName.substring(0, dotIndex);
        		outputFileName=beforeDot+"_part-"+sequenceNumber+".zip";
        	 }
        		
        	// Add file to the list to be returned
            fileList.add(outputFileName);        	
        	
            logger.debug("output file " + outputFileName);

            FileOutputStream dest = new FileOutputStream(outputFileName);
            logger.debug("dest file " + dest);
            this.out = new ZipOutputStream(new BufferedOutputStream(dest));


            // Set Default Compression level
            out.setLevel(ZipFiles.DEFAULT_COMPRESSION);
            out.setMethod(ZipOutputStream.DEFLATED);
            this.data = new byte[ZipFiles.BUFFER];
        } catch (FileNotFoundException e) {
            logger.error("File " + outputFileName + " not found !!", e);
            throw new Exception("File " + outputFileName + " not found !!", e);
        }

    }

      
   /**
    * Returns the file size of the file being zipped
    */
   public long getFileSize(){
	   return new File(outputFileName).length();
    }
    
	
    /**
     * Adds files to the zip archive.
     * 
     * @param directoryInZip - directory location inside of the zip where file will be included in the zip
     * @param filePath - location of the file to be added to the zip      
     */ 	
    public void zip(String directoryInZip, 
            String filePath) throws Exception
    {
        File thisFile = new File(filePath);

        if (thisFile.exists()) {

            try {
                FileInputStream fi = new FileInputStream(thisFile);

                origin = new BufferedInputStream(fi, BUFFER);

                ZipEntry entry = new ZipEntry(directoryInZip + File.separator
                        + thisFile.getName());
                out.putNextEntry(entry);

                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();

            } catch (FileNotFoundException e) {
                logger.error("File " + filePath + " not found !!", e);
            } catch (IOException e) {
                logger.error("Could not write to Zip File ", e);
                throw new Exception("Could not write to Zip File ", e);
            }

        } else {
            // Log message if file does not exist
            logger.info("File " + thisFile.getName()
                    + " does not exist on file system");

        }    		
    }
	
   
    public void closeFile() {

        try {
            out.flush();
            out.close();
            out = null;
        } catch (IOException e) {
            logger
                    .error(
                            "Error Flushing or closing Zipped file in Zipfiles destroy() method ",
                            e);
        }

    }
    
    /**
     * Returns a list of zip files that have been created by this object
     * There can be more than one for very large data sets
     */
    public List<String> getListOfZipFiles()
    {
    	return fileList;
    }
    
    

}
