package gov.nih.nci.caintegrator.analysis.messaging;

public class ClassComparisonLookupRequest extends ClassComparisonRequest {

	private static final long serialVersionUID = 1L;

	private ReporterGroup reporterGroup = null;
	
	
	public ClassComparisonLookupRequest(String sessionid, String taskId) {
		super(sessionid, taskId);
		
	}


	public ReporterGroup getReporterGroup() {
		return reporterGroup;
	}


	public void setReporterGroup(ReporterGroup reporterGroup) {
		this.reporterGroup = reporterGroup;
	}
	
	public String toString() {
		
		  int reporterGroupSize = -1;
		  if (reporterGroup != null) {
		    reporterGroupSize = reporterGroup.size();
		  }
		
		  int blGroupSize = -1;
		  SampleGroup baselineGroup = getBaselineGroup();
		  if (baselineGroup!=null) {
		    blGroupSize = baselineGroup.size();
		  }
		  
		  SampleGroup group1 = getGroup1();
		  int group1Size = -1;
		  if (group1 !=null) {
		    group1Size = group1.size();
		  }
			
		  String retStr = "ClassComparisonAnalysisLookupRequest: sessionId=" + getSessionId() + " taskId=" + getTaskId() + " reporterGroupSize=" + reporterGroupSize +  " blGroupSize=" + blGroupSize + " group1Szie=" + group1Size;
		  
		  if (group1 != null) { 
		    retStr += " GRP1=" + group1.getGroupName();
		  }
		  
		  if (baselineGroup != null) {
		    retStr += " baselineGroup=" + baselineGroup.getGroupName();
		  }
		  
		  return retStr;
		}

}
