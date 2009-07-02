package gov.nih.nci.caintegrator.application.zip;

import java.io.Serializable;
import java.util.List;

public class ZippingMessage implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String emailAddress;
	String zipFilename;
	String downloadType;
	String ftpURL;
	String supportPhoneNumber;
	
	// Password used to zip the file
	String userPassword;
	
	// Primary key of the zip history record for this zip
	long zipHistoryId;
	
	// List of Items that will to be zipped
	List<ZipItem> items;
	
	
	public ZippingMessage() {
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public List<ZipItem> getItems() {
		return items;
	}

	public void setItems(List<ZipItem> items) {
		this.items = items;
	}

	public String getZipFilename() {
		return zipFilename;
	}

	public void setZipFilename(String zipFilename) {
		this.zipFilename = zipFilename;
	}

	public String getDownloadType() {
		return downloadType;
	}

	public void setDownloadType(String downloadType) {
		this.downloadType = downloadType;
	}

	public String getFtpURL() {
		return ftpURL;
	}

	public void setFtpURL(String ftpURL) {
		this.ftpURL = ftpURL;
	}

	public String getSupportPhoneNumber() {
		return supportPhoneNumber;
	}

	public void setSupportPhoneNumber(String supportPhoneNumber) {
		this.supportPhoneNumber = supportPhoneNumber;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public long getZipHistoryId() {
		return zipHistoryId;
	}

	public void setZipHistoryId(long setZipHistoryId) {
		this.zipHistoryId = setZipHistoryId;
	}

}
