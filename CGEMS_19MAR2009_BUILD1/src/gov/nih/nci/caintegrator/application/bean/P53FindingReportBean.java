package gov.nih.nci.caintegrator.application.bean;

import gov.nih.nci.caintegrator.domain.finding.mutation.p53.bean.P53MutationFinding;
import gov.nih.nci.caintegrator.domain.finding.protein.ihc.bean.LevelOfExpressionIHCFinding;
import gov.nih.nci.caintegrator.studyQueryService.dto.p53.P53FindingCriteria;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class P53FindingReportBean {
	
	    private P53MutationFinding finding = null;
	    private DecimalFormat resultFormat = new DecimalFormat("0.0000");
	    private DecimalFormat sciFormat = new DecimalFormat("0.00E0");
	    private DecimalFormat tmpsciFormat = new DecimalFormat("###0.0000#####################");
	    String defaultV = "--";
	    
	    
	    /*
	     * public constructor
	     */
	    
	    public P53FindingReportBean() {}
	    
	    public P53FindingReportBean(P53MutationFinding finding){
	        this.finding = finding;
	    }
	    
	    /*
	     * patient did
	     */
	    public String getPatientDID(){
	        if(finding.getSpecimen()!=null && finding.getSpecimen().getPatientDID()!=null){
	              return finding.getSpecimen().getPatientDID();            
	        }
	        else return defaultV;       
	    }
	    
	    /*
	     * specimen id(labtrak id)
	     */
	    
	    public String getSpecimenIdentifier(){
	        if(finding.getSpecimen()!=null && finding.getSpecimen().getSpecimenIdentifier()!=null){
	              return finding.getSpecimen().getSpecimenIdentifier();            
	        }
	        else return defaultV;       
	    }
	    
	    /*
	     * time point, currently there are data for T1, T2, T4 only, no data for T3
	     */
	    public String getTimepoint(){
	        if(finding.getSpecimen()!=null && finding.getSpecimen().getTimePoint()!=null){
	            return finding.getSpecimen().getTimePoint();
	        }
	        //return null;
	       else return defaultV;
	    }
	    
	    /*
	     * mutation status
	     */
	    public String getMutationStatus() {
	        if(finding.getMutationStatus()!=null){
	            return finding.getMutationStatus();
	        }
	        else return defaultV;       
	    }

	    /*
	     * mutation type
	     */
	    
	    public String getMutationType() {
	        if(finding.getMutationType()!=null){
	            return finding.getMutationType();
	        }
	        else return defaultV;       
	    }
	    
	    /*
	     * exon or intron location
	     */
	    
	    public String getExonOrIntronLocation() {
	        if(finding.getExonOrIntronLocation()!=null){
	            return finding.getExonOrIntronLocation();
	        }
	        else return defaultV;       
	    }
	  
	    
	    /*
	     * base change
	     */
	    
	    public String getBaseChange() {
	        if(finding.getBaseChange()!=null){
	            return finding.getBaseChange();
	        }
	        else return defaultV;       
	    }
	    
	    /*
	     * codon or amino acid
	     */
	    
	    public String getCodonAminoacidChange() {
	        if(finding.getCodonAminoacidChange()!=null){
	            return finding.getCodonAminoacidChange();
	        }
	        else return defaultV;       
	    }
	    
	    /*
	     *  protein structural domain
	     */
	    
	    public String getProteinStructuralDomain() {
	        if(finding.getProteinStructuralDomain()!=null){
	            return finding.getProteinStructuralDomain();
	        }
	        else return defaultV;       
	    }
	    
	    /*
		 * this method is to create p53 report headers based on time points 
		 */
		public ArrayList<String> getHeaders(){
	        String[] myHeaders = {"Mutation Status", "Mutation Type", "Exon or Intron Location","Base Change", "Codon & Amino Acid Change","Protein Structural Domain"};        
	        ArrayList<String> headers = new ArrayList<String>(Arrays.asList(myHeaders));
	        return headers;
	    }

		
		/*
		 * patient dids are not related to time points
		 */
		 public ArrayList<String> getNonTimepointHeaders(){
		        String[] myHeaders = {"PatientDID","LabTrak ID"};        
		        ArrayList<String> headers = new ArrayList<String>(Arrays.asList(myHeaders));
		        return headers;
		    }
		 
		
			 
		 public ArrayList<String> getTimepointHeaders(P53FindingCriteria criteria){
		        Set<String> myTimepoints = criteria.getSpecimenCriteria().getTimeCourseCollection();
		        ArrayList<String> timepoints = new ArrayList<String>();
		        if(myTimepoints!=null){
		            for(String tp : myTimepoints){
		                timepoints.add(tp);
		            }            
		        }
		        else{            
		            String[] allTimepoints = {"T1", "T2","T3","T4"}; 
		            timepoints = new ArrayList<String>(Arrays.asList(allTimepoints));
		        }
		        return timepoints;
		    }

	    
}
