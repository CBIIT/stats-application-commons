package gov.nih.nci.caintegrator.application.download;


import gov.nih.nci.caarray.domain.file.FileType;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * 
 */

/**
 * @author sahnih
 *
 */
public class DownloadTask implements Serializable{
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(DownloadTask.class);
	private long startTime = -1;
	private long endTime = -1;
	private double elapsedTime = -1;
	private String cacheId;
	private String taskId;
	private DownloadStatus downloadStatus;
	private List<String> specimenList;
	private FileType type;
	private String zipFileName;
	private URL zipFileURL;
	private Long zipFileSize;
	public DownloadTask(String cacheId, String taskId, String zipFileName, FileType type, List<String> specimenList) {
		 setTaskId(taskId);
		 setCacheId(cacheId);
		 setDownloadStatus(DownloadStatus.InitiatDownload);
		 setZipFileName(zipFileName);
		 setSpecimenList(specimenList);
		 setType(type);
		 
		 //?Make sure its empty
		 if(getTaskId() == null ||
			getCacheId() == null ||
			getDownloadStatus() == null ||
			getZipFileName() == null ||
			getSpecimenList()== null ||
			getType()== null){
			 throw new NullPointerException("One or more parameters are null");
		 }
	}
	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public long getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the elapsedTime
	 */
	public double getElapsedTime() {
		if (startTime > 0){
			long currentTime = System.currentTimeMillis(); 
			elapsedTime = (currentTime - startTime)/ 1000.0;
		}
		return elapsedTime;
	}
	/**
	 * @param elapsedTime the elapsedTime to set
	 */
	public void setElapsedTime(double elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	/**
	 * @return the cacheId
	 */
	public String getCacheId() {
		return cacheId;
	}
	/**
	 * @param cacheId the cacheId to set
	 */
	public void setCacheId(String cacheId) {
		this.cacheId = cacheId;
	}
	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}
	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	/**
	 * @return the status
	 */
	public DownloadStatus getDownloadStatus() {
		return downloadStatus;
	}
	/**
	 * @param status the status to set
	 */
	public void setDownloadStatus(DownloadStatus status) {
		this.downloadStatus = status;
	}
	/**
	 * @return the zipFileName
	 */
	public String getZipFileName() {
		return zipFileName;
	}
	/**
	 * @param zipFileName the zipFileName to set
	 */
	public void setZipFileName(String zipFileName) {
		this.zipFileName = zipFileName;
	}
	/**
	 * @return the specimenList
	 */
	public List<String> getSpecimenList() {
		return specimenList;
	}
	/**
	 * @param specimenList the specimenList to set
	 */
	public void setSpecimenList(List<String> specimenList) {
		this.specimenList = validateSpecimenList(specimenList);
	}
	/**
	 * @return the type
	 */
	public FileType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(FileType type) {
		this.type = type;
	}
	private List<String> validateSpecimenList(List<String> specimenList) {
		Set<String> validatedSpecimenSet = new HashSet<String>();
		// Parses specimens to match sample names in caaaray
		for(String specimen: specimenList){
			specimen = specimen.trim();
			if(specimen.endsWith("_T") ){
				validatedSpecimenSet.add(specimen.substring(0,specimen.lastIndexOf("_T")));
			}else if(specimen.endsWith("_B") ){
				validatedSpecimenSet.add(specimen.substring(0,specimen.lastIndexOf("_B")));
			}
			else{
				validatedSpecimenSet.add(specimen);
			}
		}
		return  new ArrayList<String>(validatedSpecimenSet);
	}
	public URL getZipFileURL() {
		return zipFileURL;
	}
	public void setZipFileURL(URL zipFileURL) {
		this.zipFileURL = zipFileURL;
	}
	public Long getZipFileSize() {
		return zipFileSize;
	}
	public void setZipFileSize(Long zipFileSize) {
		this.zipFileSize = zipFileSize;
	}
}
