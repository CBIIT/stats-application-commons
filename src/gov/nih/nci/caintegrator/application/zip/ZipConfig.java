package gov.nih.nci.caintegrator.application.zip;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Encapsulates the various ZIP configuration files
 * 
 */
public class ZipConfig
 {
	private static Logger logger = Logger.getLogger(ZipConfig.class);
	private static String ZIP_PROPERTIES = "zip.properties";	
	private static ZipConfig instance;
	private Properties zipProperties;	

   
    private ZipConfig(){
    	 
    	    {
    	    	// Handle exceptions
    	    	try
    	    	 {
//    				Get the application properties from the properties file
    				  String propertiesFileName = System.getProperty(ZIP_PROPERTIES);
    				
    				  //Load the the application properties and set them as system properties
    				  zipProperties = new Properties();
    				  
    				  
    				  logger.info("Attempting to load application zip properties from file: " + propertiesFileName);
    				   
    				  FileInputStream in = new FileInputStream(propertiesFileName);
    				  zipProperties.load(in);	
    				   
    				  if (zipProperties.isEmpty()) {
    				     logger.error("Error: no properties found when loading properties file: " + propertiesFileName);
    				  }
    				  in.close();	    				  

    				
    	     	 }
    	    	catch(IOException ioe) 
    	    	{
    	    		ioe.printStackTrace();
    	    		throw new RuntimeException("Could not read configuration file");
    	    	}
    			
    	    }
    }
	/**
	 * Size at which any files larger than that size will result in a FTP
	 * download instead of HTTP
	 */
    public Double getFtpThreshold()
    {
    	return Double.parseDouble(zipProperties.getProperty("ftp_threshold"));
    }
    
    
    /**
     * Location where files zipped for HTTP download will be placed
     * 
     * @return
     */
    public String getZipLocation()
    {
    	return zipProperties.getProperty("zip_location");
    }
    
    /**
     * URL of JMS provider
     * 
     * @return
     */
    public String getMessagingUrl()
    {
    	return zipProperties.getProperty("jboss_mq_url");
    }
    
    /**
     * Location where files zipped for FTP download will be placed
     * 
     * @return
     */
    public String getFtpLocation()    
    {
    	return zipProperties.getProperty("ftp_location");
    }
    
    /**
     * URL for the secure FTP server
     * 
     * @return
     */
    public String getSecureFtpUrl()    
    {
    	return zipProperties.getProperty("ftp_secure_URL");
    }
    
    /**
     * URL for the anonymous browse FTP server
     * 
     * @return
     */
    public String getAnonymousBrowseFtpUrl()    
    {
    	return zipProperties.getProperty("ftp_anonBrowse_URL");
    }
    
    /**
     * Support phone number to include in email
     * 
     * @return
     */
    public String getSupportPhoneNumber()    
    {
    	return zipProperties.getProperty("support_phone_number");
    }    
    
    /**
     * Paramterized command used to zip files 
     * 
     * @return
     */
    public String getZipCommand()
    {
    	return zipProperties.getProperty("zip_command");
    }    

    /**
     * Where to run the zip command
     * 
     * @return
     */
    public String getZipCommandLocation() 
    {
        return zipProperties.getProperty("zip_command_location");
    }
    
    /**
     * Where to place the temporary files and directories for the zip command
     * 
     * @return
     */
    public String getTempZipLocation() 
    {
        return zipProperties.getProperty("zip_temp_location");
    }
  
    /**
     * Command to stage files for zipping
     * 
     * @return
     */
    public String getZipStagingCommand()
    {
    	return zipProperties.getProperty("zip_staging_command");
    }
    
    /**
     * Directory for the command for staging files for zipping
     * 
     * @return
     */
    public String getZipStagingCommandLocation()
    {
    	return zipProperties.getProperty("zip_staging_command_location");
    }
    
    /**
     * The type of zipper class to use
     * 
     * @return
     */
    public int getZipperType()
    {
    	return Integer.parseInt(zipProperties.getProperty("zipper_type"));    	
    }
    
   
    /**
     * Date format 
     * @return
     */
    public SimpleDateFormat getDateFormat()
    {
        
       String s = zipProperties.getProperty("date_format");
       SimpleDateFormat sdf = new  SimpleDateFormat(s);
       
       return sdf;
    }
    
    /**
     * Location of quarantined files
     * @return
     */
    public String getQuarantineLocation()
    {
        return zipProperties.getProperty("quarantine_directory");
    }


	/**
	 * @return Returns the getInstance.
	 */
	public static ZipConfig getInstance(String propertyFilename) {
		if(instance == null  || !(propertyFilename.equals(ZIP_PROPERTIES))){
    		ZIP_PROPERTIES = propertyFilename;
			instance = new ZipConfig();
		}
		return instance;
	}
	public String getFilePrefix() {
		return zipProperties.getProperty("filePrefix");
	}
   
    
    
 }
