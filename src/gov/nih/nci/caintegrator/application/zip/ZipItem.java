/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.zip;

/**
 * @author sahnih
 * All items that need to be zipped need to implement this interface
 */
public interface ZipItem {
/**
 * @return Returns the filePath.
 */
public String getFilePath();
/**
 * @param filePath The filePath to set.
 */
public void setFilePath(String filePath);
/**
 * @return Returns the filename.
 */
public String getFileName();
/**
 * @param filename The filename to set.
 */
public void setFileName(String filename);
/**
 * @return Returns the directoryInZip.
 */
public String getDirectoryInZip();
/**
 * @param directoryInZip The directoryInZip to set.
 */
public void setDirectoryInZip(String directoryInZip);
/**
 * @return the fileSize
 */
public Long getFileSize();
/**
 * @param fileSize the fileSize to set
 */
public void setFileSize(Long fileSize);
}
