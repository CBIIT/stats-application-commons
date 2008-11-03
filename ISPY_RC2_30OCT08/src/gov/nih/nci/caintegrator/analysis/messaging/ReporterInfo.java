package gov.nih.nci.caintegrator.analysis.messaging;

import java.io.Serializable;

public class ReporterInfo implements Serializable {

	private String reporterName;
	private String geneSymbol;
	private String dataFileName;
	private SampleGroup sampleGroup = null;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReporterInfo(String reporterName, String geneSymbol, String dataFileName ) {
		this.reporterName = reporterName;
		this.geneSymbol = geneSymbol;
		this.dataFileName = dataFileName;
	}

	public String getDataFileName() {
		return dataFileName;
	}

	public String getGeneSymbol() {
		return geneSymbol;
	}

	public String getReporterName() {
		return reporterName;
	}
	
	public String toString() {
	  return "ReporterInfo reporterName=" + reporterName + " geneSymbol=" + geneSymbol + " dataFileName=" + dataFileName;
	}

	public void setSampleGroup(SampleGroup sampleGroup) {
	  this.sampleGroup = sampleGroup;	
	}

	public SampleGroup getSampleGroup() {
		return sampleGroup;
	}

}
