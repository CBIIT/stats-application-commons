/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.zip;

import gov.nih.nci.caintegrator.analysis.messaging.AnalysisRequest;

public class ZipFileRequest extends AnalysisRequest{

	public ZipFileRequest(String sessionId, String taskId) {
		super(sessionId, taskId);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return null;
	}

}
