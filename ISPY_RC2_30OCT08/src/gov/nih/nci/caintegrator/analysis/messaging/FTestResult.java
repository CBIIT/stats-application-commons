package gov.nih.nci.caintegrator.analysis.messaging;

import java.io.Serializable;
import java.util.List;

public class FTestResult extends AnalysisResult implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<FTestResultEntry> ftResultEntries;
	private List<SampleGroup> sampleGroups;
	private boolean arePvaluesAdjusted = false; 
	
	public FTestResult(String sessionId, String taskId) {
		super(sessionId, taskId);
	}

	@Override
	public String toString() {
		return "FTestResult: sessionId=" + getSessionId() + " taskId=" + getTaskId() + " numResultEntries=" + getNumResultEntries();
	}

	public boolean arePvaluesAdjusted() {
		return arePvaluesAdjusted;
	}

	public void setArePvaluesAdjusted(boolean arePvaluesAdjusted) {
		this.arePvaluesAdjusted = arePvaluesAdjusted;
	}

	public List<FTestResultEntry> getResultEntries() {
		return ftResultEntries;
	}
	
	public int getNumResultEntries() { 
	  if (ftResultEntries == null) {
	    return 0;
	  }
	  return ftResultEntries.size();
	}

	public void setResultEntries(List<FTestResultEntry> ftResultEntries) {
		this.ftResultEntries = ftResultEntries;
	}

	public List<SampleGroup> getSampleGroups() {
		return sampleGroups;
	}

	public void setSampleGroups(List<SampleGroup> sampleGroups) {
		this.sampleGroups = sampleGroups;
	}

}
