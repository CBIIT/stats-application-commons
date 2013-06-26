/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.zip;


/**
 * Gets an instance of a file zipper according to settings in the config file
 * 
 * 
 */
public class FileZipperFactory {


    // Constants for zipper types
    private static final int JAVA_ZIPPER = 1;
    private static final int PASSWORD_ENCRYPT_ZIPPER = 2;

    /**
     * Determines the type of zipper to use and returns it
     * 
     * @return
     * @throws Exception
     */
    public static synchronized AbstractFileZipper getInstance(String zipPropertyFilename)
            throws Exception {

        AbstractFileZipper zipper = null;
        if(zipPropertyFilename == null ){
        	//Default is JAVA_ZIPPER
        	 zipper = new ZipFiles();
             return zipper;
        }
        ZipConfig zipConfig = ZipConfig.getInstance(zipPropertyFilename);
        if(zipConfig == null){
        	//Default is JAVA_ZIPPER
        	 zipper = new ZipFiles();
             return zipper;
        }

        switch (zipConfig.getZipperType()) {
            case JAVA_ZIPPER:
            default:
                zipper = new ZipFiles();
                break;
            case PASSWORD_ENCRYPT_ZIPPER:
            	 // Not supported at this time
        }
        return zipper;
    }

}
