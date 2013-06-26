/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.zip;

import java.util.List;

public abstract class AbstractFileZipper {

    public abstract void startNewFile(String outputFileName, int sequenceNumber) throws Exception;

    public abstract void closeFile() throws Exception;
   
    public abstract void zip(String directory, String fileName) throws Exception;    
    
    public abstract long getFileSize();
    
    public abstract List<String> getListOfZipFiles();

}
