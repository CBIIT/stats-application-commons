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
