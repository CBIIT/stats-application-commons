package gov.nih.nci.caintegrator.application.service.annotation;

import gov.nih.nci.caintegrator.enumeration.ArrayPlatformType;

import java.io.Serializable;
import java.util.List;

public class ReporterAnnotation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String reporterId;
	private List<String> geneSymbols = null;
	private List<String> genbankAccs = null;
	private List<String> locusLinkIds = null;
	private List<String> pathwayIds = null;
	private List<String> goIds = null;
	private ArrayPlatformType arrayPlatform = null;
	
	
	public ReporterAnnotation(String reporterId, ArrayPlatformType arrayPlatform) {
		this.reporterId = reporterId;
		this.arrayPlatform = arrayPlatform;
	}


	public List<String> getGeneSymbols() {
		return geneSymbols;
	}


	public void setGeneSymbols(List<String> geneSymbols) {
		this.geneSymbols = geneSymbols;
	}


	public List<String> getGOIds() {
		return goIds;
	}


	public void setGOIds(List<String> goIds) {
		this.goIds = goIds;
	}


	public List<String> getLocusLinkIds() {
		return locusLinkIds;
	}


	public void setLocusLinkIds(List<String> locusLinkIds) {
		this.locusLinkIds = locusLinkIds;
	}


	public List<String> getPathwayIds() {
		return pathwayIds;
	}


	public void setPathwayIds(List<String> pathwayIds) {
		this.pathwayIds = pathwayIds;
	}


	public String getReporterId() {
		return reporterId;
	}


	public List<String> getGenbankAccessions() {
		return genbankAccs;
	}


	public void setGenbankAccessions(List<String> genbankAccs) {
		this.genbankAccs = genbankAccs;
	}


	public ArrayPlatformType getArrayPlatform() {
		return arrayPlatform;
	}

}
