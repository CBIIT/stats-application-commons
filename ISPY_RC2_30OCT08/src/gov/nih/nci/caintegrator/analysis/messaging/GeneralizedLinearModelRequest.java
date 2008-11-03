package gov.nih.nci.caintegrator.analysis.messaging;

import gov.nih.nci.caintegrator.enumeration.CoVariateType;
import gov.nih.nci.caintegrator.enumeration.MultiGroupComparisonAdjustmentType;
import gov.nih.nci.caintegrator.enumeration.StatisticalMethodType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneralizedLinearModelRequest extends AnalysisRequest implements Serializable {	
	
	private static final long serialVersionUID = 1L;
	private StatisticalMethodType statisticalMethod;
	private CoVariateType coVariateType;
	private List<CoVariateType> coVariateTypes = new ArrayList<CoVariateType>();
	private MultiGroupComparisonAdjustmentType multiGrpComparisonAdjType = MultiGroupComparisonAdjustmentType.NONE;	
	private List<GLMSampleGroup>  comparisonGroups =  Collections.emptyList();;
	private GLMSampleGroup baselineGroup = null;
	private double foldChangeThreshold = Double.NEGATIVE_INFINITY;
	private double pValueThreshold = Double.POSITIVE_INFINITY;
    private Double geneVariance;
	
	// TODO: need to come back to do array platform
	
	public Double getGeneVariance() {
        return geneVariance;
    }
    public void setGeneVariance(Double geneVariance) {
        this.geneVariance = geneVariance;
    }
    public GeneralizedLinearModelRequest(String sessionid, String taskId) {
		super(sessionid, taskId);
	}
	public SampleGroup getBaselineGroup() {
		return baselineGroup;
	}
	public void setBaselineGroup(GLMSampleGroup baselineGroup) {
		this.baselineGroup = baselineGroup;
	}
	/*
	public CoVariateType getCoVariateType() {
		return coVariateType;
	}
	public void setCoVariateType(CoVariateType coVariateType) {
		this.coVariateType = coVariateType;
	}
	*/
	public List<CoVariateType> getCoVariateTypes() {
		return coVariateTypes;
	}
	public void setCoVariateTypes(List<CoVariateType> coVariateTypes) {
		this.coVariateTypes = coVariateTypes;
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
	public StatisticalMethodType getStatisticalMethod() {
		return statisticalMethod;
	}
	public void setStatisticalMethod(StatisticalMethodType statisticalMethod) {
		this.statisticalMethod = statisticalMethod;
	}
	
	@Override
	public String toString() {
		 int blGroupSize = -1;
		  if (baselineGroup!=null) {
		    blGroupSize = baselineGroup.size();
		  }
		  
			
		  String retStr = "GLMTestRequest: sessionId=" + getSessionId() + " taskId=" + getTaskId() + " blGroupSize=" + blGroupSize ;
		  
		  
		  if (baselineGroup != null) {
		    retStr += " baselineGroup=" + baselineGroup.getGroupName();
		  }
          for (SampleGroup sg : comparisonGroups) {
                retStr += " group=" + sg.getGroupName() + " size=" + sg.size();
          }          
		  
		  return retStr;
	}
    public List<GLMSampleGroup> getComparisonGroups() {
        return comparisonGroups;
    }
    public void setComparisonGroups(List<GLMSampleGroup> comparisonGroups) {
        this.comparisonGroups = comparisonGroups;
    }

	

}
