package gov.nih.nci.caintegrator.analysis.messaging;


public class FTestResultEntry implements ReporterEntry, java.io.Serializable {

	private static final long serialVersionUID = 1L;
	//private List<Double> groupAve = new ArrayList<Double>();
	private double maximumFoldChange;
	private double pvalue;
	private String reporterId;
	private double[] means;
	
	public FTestResultEntry() {
		super();
	}

//	public List<Double> getGroupAve() {
//		return Collections.emptyList();
//	}

	public void setGroupMeans(double[] means) {
	  this.means = means;  
	}
	
	public double[] getGroupMeans() {
	  return means;
	}
	
//	public void setGroupAverage(int index, Double value) {
//	  this.groupAve.add(index, value);
//	}

	public double getMaximumFoldChange() {
		return maximumFoldChange;
	}

	public void setMaximumFoldChange(double maximumFoldChange) {
		this.maximumFoldChange = maximumFoldChange;
	}

	public double getPvalue() {
		return pvalue;
	}

	public void setPvalue(double pvalue) {
		this.pvalue = pvalue;
	}

	public String getReporterId() {
		return reporterId;
	}

	public void setReporterId(String reporterId) {
		this.reporterId = reporterId;
	}
	 
}
