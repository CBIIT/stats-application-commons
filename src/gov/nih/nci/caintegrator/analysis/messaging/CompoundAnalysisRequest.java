package gov.nih.nci.caintegrator.analysis.messaging;

import java.util.ArrayList;
import java.util.List;

public class CompoundAnalysisRequest extends AnalysisRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<AnalysisRequest> requests = new ArrayList<AnalysisRequest>();
	
	public CompoundAnalysisRequest(String sessionId, String taskId) {
		super(sessionId, taskId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "CompoundAnalysisRequest: sessionId=" + getSessionId() + " taskId=" + getTaskId() + " numRequests=" + getNumRequests();
	}

	public List<AnalysisRequest> getRequests() {
		return requests;
	}

	public void setRequests(List<AnalysisRequest> requests) {
		this.requests = requests;
	}
	
	public void addRequest(AnalysisRequest request) {
		requests.add(request);
	}

	public int getNumRequests() { return requests.size(); }
	
}
