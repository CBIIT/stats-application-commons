package gov.nih.nci.caintegrator.analysis.messaging;

import gov.nih.nci.caintegrator.enumeration.CorrelationType;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class CategoricalCorrelationRequest extends AnalysisRequest implements Serializable {

	
	private static final long serialVersionUID = 1L;
		
	private List<DataPointVector> dataVectors = Collections.emptyList();
	
	private List<ReporterInfo> reporters = Collections.emptyList();
	
	private CorrelationType correlationType;
	
	public CategoricalCorrelationRequest(String sessionId, String taskId) {
		super(sessionId, taskId);
		
	}

	@Override
	public String toString() {		
	  StringBuffer sb = new StringBuffer();	  
	  sb.append("CategoricalCorrelationReqeust: sessionId=").append(getSessionId()).append(" taskId=" ).append(getTaskId());
	  return sb.toString();
	}

	public CorrelationType getCorrelationType() {
		return correlationType;
	}

	public void setCorrelationType(CorrelationType correlationType) {
		this.correlationType = correlationType;
	}
		
	public List<DataPointVector> getDataVectors() {
		return dataVectors;
	}

	public void setDataVectors(List<DataPointVector> dataVectors) {
		this.dataVectors = dataVectors;
	}

	public List<ReporterInfo> getReporters() {
		return reporters;
	}

	public void setReporters(List<ReporterInfo> reporters) {
		this.reporters = reporters;
	}
				
}
