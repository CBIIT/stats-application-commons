package gov.nih.nci.caintegrator.analysis.messaging;

import java.io.Serializable;

public class GeneralizedLinearModelResultEntry implements ReporterEntry, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * reporter id shown on the class comparison report
	 */ 
	private String reporterId;
	
	
	/*
	 *  p-value on the class comparison report page
	 */
	
	private double[] groupPvalues;
	
	
	/*
	 *  (non-Javadoc)
	 * @see gov.nih.nci.caintegrator.analysis.messaging.ReporterEntry#getReporterId()
	 */

	public String getReporterId() {
		return reporterId;		
	}
	
	public GeneralizedLinearModelResultEntry() {
		super();
	}

	public void setReporterId(String reporterId) {
		this.reporterId = reporterId;
	}

    public double[] getGroupPvalues() {
        return groupPvalues;
    }

    public void setGroupPvalues(double[] groupPvalues) {
        this.groupPvalues = groupPvalues;
    }
	
	

}
