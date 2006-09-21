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
    public static synchronized AbstractFileZipper getInstance()
            throws Exception {

        AbstractFileZipper zipper;
        switch (ZipConfig.getZipperType()) {
            case JAVA_ZIPPER:
                zipper = new ZipFiles();
                break;
            case PASSWORD_ENCRYPT_ZIPPER:
            	 // Not supported at this time
            default:
            	throw new Exception("Unknown zipper type");
        }
        return zipper;
    }

}
