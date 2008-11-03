package gov.nih.nci.caintegrator.analysis.messaging;

import java.util.HashMap;

import gov.nih.nci.caintegrator.analysis.messaging.SampleGroup;

public class GLMSampleGroup extends SampleGroup {

	//[sid] => map of annotations ( [age] => 35, [gender] => M , ...)
	public HashMap<String, HashMap> annotationMap;
	
	public GLMSampleGroup(String groupName) {
		super(groupName, 50);
		annotationMap = new HashMap();

	}
	
	public void addPatientData(String sid, HashMap<String, String> am){
		setPatientInAnnotationMap(sid, am);
	}

	public HashMap getPatientFromAnnotationMap(String sid)	{
		return annotationMap.get(sid);
	}
	
	public void setPatientInAnnotationMap(String key, HashMap value)	{
		annotationMap.put(key, value);
	}
	
	public HashMap<String, HashMap> getAnnotationMap() {
		return annotationMap;
	}

	public void setAnnotationMap(HashMap<String, HashMap> annotationMap) {
		this.annotationMap = annotationMap;
	}
}
