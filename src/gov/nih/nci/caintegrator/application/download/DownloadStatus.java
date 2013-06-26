/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.download;
/**
 * Comment this
 * @author SahniH
 *
 */



public enum DownloadStatus {
	InitiatDownload, // Connected to server , Searching for experiment 
    SearchingForExperiment,// searching for Experiment
	PopulateHybridizations, //populate hybridization associated with experiment
	GettingDataFiles, // getting raw data files associated with each hybridization
	DownloadingFiles, // reading File from caArray and writing it to drive
	ZippingFiles,	// zipping the download bundle
    Error,  // error or exception encountered
    Completed, //download completed
	NoFilesFoundToDownload;  //If no files are found to download
	
	//Describe any error or exception message
	String comment;

	/**
	 * @return Returns the comment.
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment The comment to set.
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
}
