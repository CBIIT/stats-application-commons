package gov.nih.nci.caintegrator.analysis.messaging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategoricalCorrelationResult extends AnalysisResult implements Serializable {

	
	private static final long serialVersionUID = 1L;

	private List<DataPointVector> dataVectors = new ArrayList<DataPointVector>();
	

	public CategoricalCorrelationResult(String sessionId, String taskId) {
		super(sessionId, taskId);
	}
	
	@Override
	public String toString() {
		 return "CorrelationResult: sessionId=" + getSessionId() + " taskId=" + getTaskId();
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
