/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

/*
 * ZipTest.java
 * sahnih,Prashant
 * $Revision: 1.1 $
 * $Date: 2006-09-12 00:49:25 $
 * $Author: sahnih $
 * $Name: not supported by cvs2svn $
 * $Id: ZipTest.java,v 1.1 2006-09-12 00:49:25 sahnih Exp $
 * Based on NCIA ZipTest
 */
package gov.nih.nci.caintegrator.application.zip.test;




import gov.nih.nci.caintegrator.application.zip.ZipItem;
import gov.nih.nci.caintegrator.application.zip.ZipManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * Add one sentence class summary here
 * Add class description here
 * @author sahnih
 * @version 1.0, Aug 30, 2006
 *
**/
public class ZipTest extends TestCase {
	private List<ZipItem> zipItemCollection; 
	private String inputZipDirectory = "L:\\caIntegrator\\testing\\test data\\ZipTest\\input";
	private String outputZipDirectory = "L:\\caIntegrator\\testing\\test data\\ZipTest\\output";
	private String targetZipFileName = "zipTest.zip";
	/**
	 * Constructor for QueryTest.
	 * @param name
	 */
	public ZipTest(String name) {
		super(name);
	}

	/*
	 * @see TestCase#setUp()
	 */

	protected void setUp() throws Exception {
	    // Create ZipItemList 
	    File f = new File(inputZipDirectory);
	    if (f.exists() && f.isDirectory()) {
		    String files[] = f.list();
		    zipItemCollection = new ArrayList<ZipItem>(); 
		    for (int i = 0; i < files.length; i++) {
		    	  	ZipItem testZipItem = new TestZipItemImpl();
		    	  	testZipItem.setFileName(files[i]);
		  			testZipItem.setFilePath(f.getAbsolutePath()+File.separator+files[i]);
		  			testZipItem.setDirectoryInZip("CGEMS");
		  			zipItemCollection.add(testZipItem);
		    }
	    }
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testZipping() {
	    try{
	    	//Delete file if it already exsists
	    	File zipFile = new File(outputZipDirectory+File.separator+targetZipFileName);
            if(zipFile.exists()){
            	zipFile.delete();
            }
            
	        // Test ZipManager Class
			// start zipping and wait for it to finish
			ZipManager zipper = new ZipManager();
			zipper.setName(targetZipFileName);
			zipper.setItems(zipItemCollection);
	        zipper.setTarget(outputZipDirectory+File.separator+targetZipFileName);
            zipper.setBreakIntoMultipleFileIfLarge(false);
            zipper.start();
	        //	Verify if the new file was created
            while (zipper.isAlive()) {
            }
	    	File newZipFile = new File(outputZipDirectory+File.separator+targetZipFileName);
            assertTrue(newZipFile.exists());
            assertTrue(newZipFile.isFile());
            assertTrue(newZipFile.length() > 0);
	        
		
        } catch (Exception e) {
            e.printStackTrace();
        }
	}


}
