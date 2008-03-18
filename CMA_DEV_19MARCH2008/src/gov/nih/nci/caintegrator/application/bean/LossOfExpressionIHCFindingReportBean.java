package gov.nih.nci.caintegrator.application.bean;

import gov.nih.nci.caintegrator.domain.finding.protein.ihc.bean.LossOfExpressionIHCFinding;
import gov.nih.nci.caintegrator.studyQueryService.dto.ihc.LossOfExpressionIHCFindingCriteria;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class LossOfExpressionIHCFindingReportBean implements IHCFindingReportBean{
    private LossOfExpressionIHCFinding finding = null;    
    private DecimalFormat resultFormat = new DecimalFormat("0.0000");
    private DecimalFormat sciFormat = new DecimalFormat("0.00E0");
    private DecimalFormat tmpsciFormat = new DecimalFormat("###0.0000#####################");
    String defaultV = "--";

    public LossOfExpressionIHCFindingReportBean() {}
    
    public LossOfExpressionIHCFindingReportBean(LossOfExpressionIHCFinding finding){
        this.finding = finding;
    }
    
    
    
    public String getPatientDID(){
        if(finding.getSpecimen()!=null && finding.getSpecimen().getPatientDID()!=null){
              return finding.getSpecimen().getPatientDID();            
        }
        else return defaultV;       
    }
    
    public String getSpecimenIdentifier(){
        if(finding.getSpecimen()!=null && finding.getSpecimen().getSpecimenIdentifier()!=null){
            return finding.getSpecimen().getSpecimenIdentifier();
        }
        else return defaultV;
    }
    
    public String getTimepoint(){
        if(finding.getSpecimen()!=null && finding.getSpecimen().getTimePoint()!=null){
            return finding.getSpecimen().getTimePoint();
        }
        else return defaultV;
    }
    
    public String getBiomarkerName(){
        if(finding.getProteinBiomarker().getName()!=null){
            return finding.getProteinBiomarker().getName();
        }
        else return defaultV;        
    }
    
    public String getBenignPresentValue() {
        if(finding.getBenignPresentValue()!=null){
            return finding.getBenignPresentValue();
        }
        else return defaultV;       
    }
    
    public String getBenignSum(){
        String benignSum = defaultV;
        if(finding.getBenignSum()!=null){
            benignSum = finding.getBenignSum().toString();
            return benignSum;
        }
        return benignSum;
    }
    
    public String getInvasiveSum(){
        String invasiveSum = defaultV;
        if(finding.getInvasiveSum()!=null){
            invasiveSum = finding.getInvasiveSum().toString();
            return invasiveSum;
        }
        return invasiveSum;
    }
    
    public String getInvasiveBenignDiff() {
        if(finding.getInvasiveBenignDiff()!=null){
            return finding.getInvasiveBenignDiff().toString();
        }
        else return defaultV;
    }
    
    public String getComments() {
        if(finding.getComments()!=null){
            return finding.getComments();
        }
        else return defaultV;
    }
    
    public String getLossResult() {
        if(finding.getLossResult()!=null){
            return finding.getLossResult();
        }
        else return defaultV;
    }    
   

    public ArrayList<String> getHeaders(){
        String[] myHeaders = {"Benign Present Value", "Invasive Sum", "Benign Sum", "Invasive-Benign Difference","Comments","Result Code"};        
        ArrayList<String> headers = new ArrayList<String>(Arrays.asList(myHeaders));
        return headers;
    }
    
    public ArrayList<String> getNonTimepointHeaders(){
        String[] myHeaders = {"PatientDID", "Biomarker"};        
        ArrayList<String> headers = new ArrayList<String>(Arrays.asList(myHeaders));
        return headers;
    }
    
    public ArrayList<String> getTimepointHeaders(LossOfExpressionIHCFindingCriteria criteria){
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
