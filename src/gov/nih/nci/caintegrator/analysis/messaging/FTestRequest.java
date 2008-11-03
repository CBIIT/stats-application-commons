package gov.nih.nci.caintegrator.analysis.messaging;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import gov.nih.nci.caintegrator.enumeration.ArrayPlatformType;
import gov.nih.nci.caintegrator.enumeration.MultiGroupComparisonAdjustmentType;
import gov.nih.nci.caintegrator.enumeration.StatisticalMethodType;

public class FTestRequest extends AnalysisRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private double foldChangeThreshold = Double.NEGATIVE_INFINITY;
	
	private double pValueThreshold = Double.POSITIVE_INFINITY;
	
	private ArrayPlatformType arrayPlatform = ArrayPlatformType.AFFY_OLIGO_PLATFORM;
	
	private MultiGroupComparisonAdjustmentType multiGrpComparisonAdjType = MultiGroupComparisonAdjustmentType.NONE;
	
	private StatisticalMethodType statisticalMethod;
	
	private List<SampleGroup> sampleGroups = Collections.emptyList();
	
	public FTestRequest(String sessionId, String taskId) {
		super(sessionId, taskId);
	}

	@Override
	public String toString() {
	  StringBuffer sb = new StringBuffer();
	 
	  sb.append("FTestRequest sessionId=").append(getSessionId()).append(" taskId=").append(getTaskId());
	  
	  for (SampleGroup sg : sampleGroups) {
	    sb.append(" group=").append(sg.getGroupName()).append(" size=").append(sg.size());
	  }
	  return sb.toString();
	}

	public ArrayPlatformType getArrayPlatform() {
		return arrayPlatform;
	}

	public void setArrayPlatform(ArrayPlatformType arrayPlatform) {
		this.arrayPlatform = arrayPlatform;
	}

	public double getFoldChangeThreshold() {
		return foldChangeThreshold;
	}

	public void setFoldChangeThreshold(double foldChangeThreshold) {
		this.foldChangeThreshold = foldChangeThreshold;
	}

	public MultiGroupComparisonAdjustmentType getMultiGrpComparisonAdjType() {
		return multiGrpComparisonAdjType;
	}

	public void setMultiGrpComparisonAdjType(
			MultiGroupComparisonAdjustmentType multiGrpComparisonAdjType) {
		this.multiGrpComparisonAdjType = multiGrpComparisonAdjType;
	}

	public double getPValueThreshold() {
		return pValueThreshold;
	}

	public void setPValueThreshold(double valueThreshold) {
		pValueThreshold = valueThreshold;
	}

	public List<SampleGroup> getSampleGroups() {
		return sampleGroups;
	}

	public void setSampleGroups(List<SampleGroup> sampleGroups) {
		this.sampleGroups = sampleGroups;
	}

	public StatisticalMethodType getStatisticalMethod() {
		return statisticalMethod;
	}

	public void setStatisticalMethod(StatisticalMethodType statisticalMethod) {
		this.statisticalMethod = statisticalMethod;
	}

}
