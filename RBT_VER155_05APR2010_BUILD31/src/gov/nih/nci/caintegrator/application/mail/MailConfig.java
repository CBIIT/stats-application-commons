package gov.nih.nci.caintegrator.application.mail;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Encapsulates the various MAIL configuration files
 * 
 */
public class MailConfig
 {
	private static Logger logger = Logger.getLogger(MailConfig.class);
	public static String MAIL_PROPERTIES = "mail.properties"; 	
	private static Properties mailProperties;
	private static MailConfig instance;
    private MailConfig(){
    	// Handle exceptions
    	try
    	 {
    		  System.out.println(MAIL_PROPERTIES);
			  // Get the application properties from the properties file
			  String propertiesFileName = System.getProperty(MAIL_PROPERTIES);
			
			  //Load the the application properties and set them as system properties
			  mailProperties = new Properties();
			  
			  
			  logger.info("Attempting to load application mail properties from file: " + propertiesFileName);
			   
			  FileInputStream in = new FileInputStream(propertiesFileName);
			  mailProperties.load(in);	
			   
			  if (mailProperties.isEmpty()) {
			     logger.error("Error: no properties found when loading properties file: " + propertiesFileName);
			  }
			  in.close();	    				  
			
     	 }
    	catch(IOException ioe) 
    	{
    		ioe.printStackTrace();
    		throw new RuntimeException("Could not read mail configuration file");
    	}
		
    }
	/**
	 * @return Returns the appSupportNumber.
	 */
	public String getAppSupportNumber() {
		return mailProperties.getProperty("techSupportNumber");
	}
	/**
	 * @return Returns the fileRetentionPeriodInDays.
	 */
	public String getFileRetentionPeriodInDays() {
		return mailProperties.getProperty("fileRetentionPeriodInDays");
	}
	/**
	 * @return Returns the from.
	 */
	public String getFrom() {
		return mailProperties.getProperty( "fromAddress" );
	}
	/**
	 * @return Returns the ftpHostnameAndPort.
	 */
	public String getFtpHostnameAndPort() {
		return mailProperties.getProperty("ftpHostnameAndPort");
	}
	/**
	 * @return Returns the ftpSubject.
	 */
	public String getFtpSubject() {
		return mailProperties.getProperty("ftp.Subject");
	}
	/**
	 * @return Returns the ftpUnformattedBody1.
	 */
	public String getFtpUnformattedBody1() {
		return mailProperties.getProperty("ftp.Body1");
	}
	/**
	 * @return Returns the ftpUnformattedBody2.
	 */
	public String getFtpUnformattedBody2() {
		return mailProperties.getProperty("ftp.Body2");
	}
	/**
	 * @return Returns the ftpUnformattedBody3.
	 */
	public String getFtpUnformattedBody3() {
		return mailProperties.getProperty("ftp.Body3");
	}
	/**
	 * @return Returns the ftpUnformattedBody4.
	 */
	public String getFtpUnformattedBody4() {
		return mailProperties.getProperty("ftp.Body4");
	}
	/**
	 * @return Returns the feedback template.
	 */
	public String getUnformattedFeedback() {
		return mailProperties.getProperty("feedback.template");
	}
	/**
	 * @return Returns the feedback mail to address.
	 */
	public String getFeedbackAddress() {
		return mailProperties.getProperty("feedback.mailTo");
	}
	/**
	 * @return Returns the feedback subject.
	 */
	public String getFeedbackSubject() {
		return mailProperties.getProperty("feedback.mailSubject");
	}
	/**
	 * @return Returns the host.
	 */
	public String getHost() {
		return mailProperties.getProperty( "host" );
	}
	/**
	 * @return Returns the mAIL_PROPERTIES.
	 */
	public String getMAIL_PROPERTIES() {
		return MAIL_PROPERTIES;
	}
	/**
	 * @return Returns the mailBody.
	 */
	public String getMailBody() {
		return mailProperties.getProperty("spreadsheet.mailBody");
	}
	/**
	 * @return Returns the mailSubject.
	 */
	public String getMailSubject() {
		return mailProperties.getProperty("spreadsheet.mailSubject");
	}
	/**
	 * @return Returns the userRequestMail address.
	 */
	public String getUserRequestMail() {
		return mailProperties.getProperty("userRequestMail");
	}
	/**
	 * @return Returns the userRequestCC address.
	 */
	public String getUserRequestCC() {
		return mailProperties.getProperty("userRequestCC");
	}
	/**
	 * @return Returns the requestSubject.
	 */
	public String getRequestSubject() {
		return mailProperties.getProperty("request.Subject");
	}
	/**
	 * @return Returns the requestUnformattedBody.
	 */
	public String getRequestUnformattedBody() {
		return mailProperties.getProperty("request.Body");
	}
	/**
	 * @return Returns the registerSubject.
	 */
	public String getRegisterSubject() {
		return mailProperties.getProperty("register.Subject");
	}
	/**
	 * @return Returns the registerUnformattedBody.
	 */
	public String getRegisterUnformattedBody() {
		return mailProperties.getProperty("register.Body");
	}
	/**
	 * @return Returns the techSupportEndTime.
	 */
	public String getTechSupportEndTime() {
		return mailProperties.getProperty("techSupportEndTime");
	}
	/**
	 * @return Returns the techSupportStartTime.
	 */
	public String getTechSupportStartTime() {
		return mailProperties.getProperty("techSupportStartTime");
	}
	/**
	 * @return Returns the acronym.
	 */
	public String getAcronym() {
		return mailProperties.getProperty("acronym");
	}
	/**
	 * @return Returns the project.
	 */
	public String getProject() {
		return mailProperties.getProperty("project");
	}
	/**
	 * @return Returns the techSupportURL.
	 */
	public String getTechSupportURL() {
		return mailProperties.getProperty("techSupportURL");
	}
	/**
	 * @return Returns the techSupportMail.
	 */
	public String getTechSupportMail() {
		return mailProperties.getProperty("techSupportMail");
	}
	/**
	 * @return Returns the ftpSubject.
	 */
	public String getFtpErrorSubject() {
		return mailProperties.getProperty("ftp.error.Subject");
	}
	/**
	 * @return Returns the ftpUnformattedBody1.
	 */
	public String getFtpUnformattedErrorBody1() {
		return mailProperties.getProperty("ftp.error.Body1");
	}
	/**
	 * @return Returns the ftpUnformattedBody2.
	 */
	public String getFtpUnformattedErrorBody2() {
		return mailProperties.getProperty("ftp.error.Body2");
	}
	/**
	 * @return Returns the getInstance.
	 */
	public static MailConfig getInstance(String propertyFilename) {
		if(instance == null  || !(propertyFilename.equals(MAIL_PROPERTIES))){
			MAIL_PROPERTIES = propertyFilename;
			instance = new MailConfig();
		}
		return instance;
	}
 }
