package gov.nih.nci.caintegrator.analysis.messaging;

import gov.nih.nci.caintegrator.enumeration.CorrelationType;

import java.io.Serializable;
import java.util.List;

public class CorrelationRequest extends AnalysisRequest implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private List<DataPoint> vector1 = null;
	private String vector1Name = null;
	
	
	private List<DataPoint> vector2 = null;
	private String vector2Name = null;
	
	private ReporterInfo reporter1 = null;
	private ReporterInfo reporter2 = null;
	
	//the patientIds list contains the patient id corresponding
	//to the sampleId in the sampleId list. The patient id list
	//should always be the same length as the sampleId list 
	//the patientId list can have repeated entries (when two samples
	//come from the same pateint)
	
	//the sampleIds are used together with reporter names to look up expression values for 
	//reporters in the R binary file.
	
	private IdList sampleIds = null;
	private IdList patientIds = null;
	
	private CorrelationType correlationType;
	
	public CorrelationRequest(String sessionId, String taskId) {
		super(sessionId, taskId);
		
	}
	
	public void setReporter1(ReporterInfo reporter1) {
	  this.reporter1 = reporter1;
	}
	
	public void setReporter2(ReporterInfo reporter2) {
	  this.reporter2 = reporter2;
	}

	@Override
	public String toString() {
		
	  StringBuffer sb = new StringBuffer();
	  	
	  sb.append("CorrelationReqeust: sessionId=").append(getSessionId()).append(" taskId=" ).append(getTaskId());
	  
	  
	  //sb.append(" sampleIds.size()=").append(sampleIds.size());
	  //sb.append(" patientIds=").append(patientIds);
	  if (vector1 != null) {
	    sb.append(" vector1.size()=").append(vector1.size());
	  }
	  else {
	    sb.append(" vector1=null");
	  }
	  
	  if (vector2 != null) {
	    sb.append(" vector2.size()=").append(vector2.size());
	  }
	  else {
	    sb.append(" vector2=null");
	  }
	  
	  sb.append(" reporter1=").append(reporter1);
	  sb.append(" reporter2=").append(reporter2);
	 
	  sb.append(" ").append(correlationType.toString());
	 
	  return sb.toString();
	  
	}

	public CorrelationType getCorrelationType() {
		return correlationType;
	}

	public void setCorrelationType(CorrelationType correlationType) {
		this.correlationType = correlationType;
	}

	
	public ReporterInfo getReporter1() {
		return reporter1;
	}

	public ReporterInfo getReporter2() {
		return reporter2;
	}

	public IdList getPatientIds() {
		return patientIds;
	}

	public void setPatientIds(IdList patientIds) {
		this.patientIds = patientIds;
	}

	public IdList getSampleIds() {
		return sampleIds;
	}

	public void setSampleIds(IdList sampleIds) {
		this.sampleIds = sampleIds;
	}

	public String getVector1Name() {
		return vector1Name;
	}

	public void setVector1Name(String vector1Name) {
		this.vector1Name = vector1Name;
	}

	public String getVector2Name() {
		return vector2Name;
	}

	public void setVector2Name(String vector2Name) {
		this.vector2Name = vector2Name;
	}

	public void setVector1(List<DataPoint> vector1) {
		this.vector1 = vector1;
	}

	public void setVector2(List<DataPoint> vector2) {
		this.vector2 = vector2;
	}

	public List<DataPoint> getVector1() {
		return vector1;
	}

	public List<DataPoint> getVector2() {
		return vector2;
	}
	
	
	
}
