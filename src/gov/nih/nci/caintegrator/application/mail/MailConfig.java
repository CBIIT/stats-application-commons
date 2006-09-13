package gov.nih.nci.caintegrator.application.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Encapsulates the various MAIL configuration files
 * 
 */
public class MailConfig
 {
	public static String MAIL_PROPERTIES = "mail.properties"; 	
	private static Properties mailProperties;
    static
    {
    	// Handle exceptions
    	try
    	 {
			mailProperties = new Properties();
			InputStream in = Thread.currentThread().getContextClassLoader()
								.getResourceAsStream(MAIL_PROPERTIES);
			mailProperties.load(in);		
			in.close();
			
     	 }
    	catch(IOException ioe) 
    	{
    		ioe.printStackTrace();
    		throw new RuntimeException("Could not read configuration file");
    	}
		
    }
	/**
	 * @return Returns the appSupportNumber.
	 */
	public static String getAppSupportNumber() {
		return mailProperties.getProperty("techSupportNumber");
	}
	/**
	 * @return Returns the fileRetentionPeriodInDays.
	 */
	public static String getFileRetentionPeriodInDays() {
		return mailProperties.getProperty("fileRetentionPeriodInDays");
	}
	/**
	 * @return Returns the from.
	 */
	public static String getFrom() {
		return mailProperties.getProperty( "fromAddress" );
	}
	/**
	 * @return Returns the ftpHostnameAndPort.
	 */
	public static String getFtpHostnameAndPort() {
		return mailProperties.getProperty("ftpHostnameAndPort");
	}
	/**
	 * @return Returns the ftpSubject.
	 */
	public static String getFtpSubject() {
		return mailProperties.getProperty("ftp.Subject");
	}
	/**
	 * @return Returns the ftpUnformattedBody1.
	 */
	public static String getFtpUnformattedBody1() {
		return mailProperties.getProperty("ftp.Body1");
	}
	/**
	 * @return Returns the ftpUnformattedBody2.
	 */
	public static String getFtpUnformattedBody2() {
		return mailProperties.getProperty("ftp.Body2");
	}
	/**
	 * @return Returns the ftpUnformattedBody3.
	 */
	public static String getFtpUnformattedBody3() {
		return mailProperties.getProperty("ftp.Body3");
	}
	/**
	 * @return Returns the ftpUnformattedBody4.
	 */
	public static String getFtpUnformattedBody4() {
		return mailProperties.getProperty("ftp.Body4");
	}
	/**
	 * @return Returns the host.
	 */
	public static String getHost() {
		return mailProperties.getProperty( "host" );
	}
	/**
	 * @return Returns the mAIL_PROPERTIES.
	 */
	public static String getMAIL_PROPERTIES() {
		return MAIL_PROPERTIES;
	}
	/**
	 * @return Returns the mailBody.
	 */
	public static String getMailBody() {
		return mailProperties.getProperty("spreadsheet.mailBody");
	}
	/**
	 * @return Returns the mailSubject.
	 */
	public static String getMailSubject() {
		return mailProperties.getProperty("spreadsheet.mailSubject");
	}
	/**
	 * @return Returns the registerSubject.
	 */
	public static String getRegisterSubject() {
		return mailProperties.getProperty("register.Subject");
	}
	/**
	 * @return Returns the registerUnformattedBody.
	 */
	public static String getRegisterUnformattedBody() {
		return mailProperties.getProperty("register.Body");
	}
	/**
	 * @return Returns the techSupportEndTime.
	 */
	public static String getTechSupportEndTime() {
		return mailProperties.getProperty("techSupportEndTime");
	}
	/**
	 * @return Returns the techSupportStartTime.
	 */
	public static String getTechSupportStartTime() {
		return mailProperties.getProperty("techSupportStartTime");
	}
	/**
	 * @return Returns the acronym.
	 */
	public static String getAcronym() {
		return mailProperties.getProperty("acronym");
	}
	/**
	 * @return Returns the project.
	 */
	public static String getProject() {
		return mailProperties.getProperty("project");
	}
	/**
	 * @return Returns the techSupportURL.
	 */
	public static String getTechSupportURL() {
		return mailProperties.getProperty("techSupportURL");
	}
	/**
	 * @return Returns the techSupportMail.
	 */
	public static String getTechSupportMail() {
		return mailProperties.getProperty("techSupportMail");
	}
 }
