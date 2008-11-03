package gov.nih.nci.caintegrator.analysis.messaging;

import java.util.ArrayList;
import java.util.List;


/**
 * This class is sent back from the analysis server in response 
 * to a lookup request.
 * @author harrismic
 *
 */
public class ExpressionLookupResult extends AnalysisResult {

	private static final long serialVersionUID = 1L;
	
	private List<DataPointVector> dataVectors = new ArrayList<DataPointVector>();
	
	public ExpressionLookupResult(String sessionId, String taskId) {
		super(sessionId, taskId);		
	}

	@Override
	public String toString() {
		int numDataVectors = 0;
		if (dataVectors != null) {
		  numDataVectors = dataVectors.size();
		}
		return "ExpressionLookupResult sessionId=" + getSessionId() + " taskId=" + getTaskId() + " numDataPoints=" + numDataVectors; 
	}

	
	
	public List<DataPointVector> getDataVectors() {
		return dataVectors;
	}

	public void setDataVectors(List<DataPointVector> dataVectors) {
		this.dataVectors = dataVectors;
	}
	
	public void addDataVector(DataPointVector vector) {
	  dataVectors.add(vector);
	}
	
	

}
