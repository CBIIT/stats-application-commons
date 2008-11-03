package gov.nih.nci.caintegrator.analysis.messaging;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class CorrelationResult extends AnalysisResult implements Serializable {

	
	private static final long serialVersionUID = 1L;

	private Double correlationValue = null;
	
	private String group1Name ;
	private String group2Name ;
	
	private List<DataPoint> dataPoints = Collections.emptyList();
	
	public CorrelationResult(String sessionId, String taskId) {
		super(sessionId, taskId);
	}

	public Double getCorrelationValue() {
		return correlationValue;
	}

	public void setCorrelationValue(Double correlationValue) {
		this.correlationValue = correlationValue;
	}

	
	@Override
	public String toString() {
		 return "CorrelationResult: sessionId=" + getSessionId() + " taskId=" + getTaskId() + " group1Name=" + getGroup1Name() + " group2Name=" + getGroup2Name() + " corrValue=" + correlationValue;
	}

	public List<DataPoint> getDataPoints() {
		return dataPoints;
	}

	public void setDataPoints(List<DataPoint> dataPoints) {
		this.dataPoints = dataPoints;
	}

	public String getGroup1Name() {
		return group1Name;
	}

	public void setGroup1Name(String name) {
		group1Name = name;
	}

	public String getGroup2Name() {
		return group2Name;
	}

	public void setGroup2Name(String name) {
		group2Name = name;
	}

}
