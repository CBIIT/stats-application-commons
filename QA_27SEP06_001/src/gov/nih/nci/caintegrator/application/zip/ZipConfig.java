package gov.nih.nci.caintegrator.application.zip;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * Encapsulates the various ZIP configuration files
 * 
 */
public class ZipConfig
 {
	private static String ZIP_PROPERTIES = "zip.properties";	
	private static Properties zipProperties;	
	
    static
    {
    	// Handle exceptions
    	try
    	 {
			zipProperties = new Properties();
			InputStream in = Thread.currentThread().getContextClassLoader()
								.getResourceAsStream(ZIP_PROPERTIES);
			zipProperties.load(in);		
			in.close();
			
     	 }
    	catch(IOException ioe) 
    	{
    		ioe.printStackTrace();
    		throw new RuntimeException("Could not read configuration file");
    	}
		
    }
	/**
	 * Size at which any files larger than that size will result in a FTP
	 * download instead of HTTP
	 */
    public static Double getFtpThreshold()
    {
    	return Double.parseDouble(zipProperties.getProperty("ftp_threshold"));
    }
    
    
    /**
     * Location where files zipped for HTTP download will be placed
     * 
     * @return
     */
    public static String getZipLocation()
    {
    	return zipProperties.getProperty("zip_location");
    }
    
    /**
     * URL of JMS provider
     * 
     * @return
     */
    public static String getMessagingUrl()
    {
    	return zipProperties.getProperty("jboss_mq_url");
    }
    
    /**
     * Location where files zipped for FTP download will be placed
     * 
     * @return
     */
    public static String getFtpLocation()    
    {
    	return zipProperties.getProperty("ftp_location");
    }
    
    /**
     * URL for the secure FTP server
     * 
     * @return
     */
    public static String getSecureFtpUrl()    
    {
    	return zipProperties.getProperty("ftp_secure_URL");
    }
    
    /**
     * URL for the anonymous browse FTP server
     * 
     * @return
     */
    public static String getAnonymousBrowseFtpUrl()    
    {
    	return zipProperties.getProperty("ftp_anonBrowse_URL");
    }
    
    /**
     * Support phone number to include in email
     * 
     * @return
     */
    public static String getSupportPhoneNumber()    
    {
    	return zipProperties.getProperty("support_phone_number");
    }    
    
    /**
     * Paramterized command used to zip files 
     * 
     * @return
     */
    public static String getZipCommand()
    {
    	return zipProperties.getProperty("zip_command");
    }    

    /**
     * Where to run the zip command
     * 
     * @return
     */
    public static String getZipCommandLocation() 
    {
        return zipProperties.getProperty("zip_command_location");
    }
    
    /**
     * Where to place the temporary files and directories for the zip command
     * 
     * @return
     */
    public static String getTempZipLocation() 
    {
        return zipProperties.getProperty("zip_temp_location");
    }
  
    /**
     * Command to stage files for zipping
     * 
     * @return
     */
    public static String getZipStagingCommand()
    {
    	return zipProperties.getProperty("zip_staging_command");
    }
    
    /**
     * Directory for the command for staging files for zipping
     * 
     * @return
     */
    public static String getZipStagingCommandLocation()
    {
    	return zipProperties.getProperty("zip_staging_command_location");
    }
    
    /**
     * The type of zipper class to use
     * 
     * @return
     */
    public static int getZipperType()
    {
    	return Integer.parseInt(zipProperties.getProperty("zipper_type"));    	
    }
    
   
    /**
     * Date format 
     * @return
     */
    public static SimpleDateFormat getDateFormat()
    {
        
       String s = zipProperties.getProperty("date_format");
       SimpleDateFormat sdf = new  SimpleDateFormat(s);
       
       return sdf;
    }
    
    /**
     * Location of quarantined files
     * @return
     */
    public static String getQuarantineLocation()
    {
        return zipProperties.getProperty("quarantine_directory");
    }
   
    
    
 }
