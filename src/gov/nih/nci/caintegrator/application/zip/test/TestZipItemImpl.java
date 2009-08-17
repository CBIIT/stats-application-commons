/**
 * 
 */
package gov.nih.nci.caintegrator.application.zip.test;

import gov.nih.nci.caintegrator.application.zip.ZipItem;

/**
 * @author sahnih
 *
 */
public class TestZipItemImpl implements ZipItem{
	private String filePath;
	private String fileName;
	private String directoryInZip;
	private Long fileSize;
	/**
	 * @return Returns the directoryInZip.
	 */
	public String getDirectoryInZip() {
		return directoryInZip;
	}
	/**
	 * @param directoryInZip The directoryInZip to set.
	 */
	public void setDirectoryInZip(String directoryInZip) {
		this.directoryInZip = directoryInZip;
	}
	/**
	 * @return Returns the fileName.
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName The fileName to set.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return Returns the filePath.
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * @param filePath The filePath to set.
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	/**
	 * @return the fileSize
	 */
	public Long getFileSize() {
		return fileSize;
	}
	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}


}
