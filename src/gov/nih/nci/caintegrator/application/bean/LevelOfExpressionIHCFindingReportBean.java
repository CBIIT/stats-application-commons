package gov.nih.nci.caintegrator.application.bean;

import gov.nih.nci.caintegrator.domain.finding.protein.ihc.bean.LevelOfExpressionIHCFinding;
import gov.nih.nci.caintegrator.studyQueryService.dto.ihc.LevelOfExpressionIHCFindingCriteria;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class LevelOfExpressionIHCFindingReportBean implements IHCFindingReportBean{
    private LevelOfExpressionIHCFinding finding = null;
    private DecimalFormat resultFormat = new DecimalFormat("0.0000");
    private DecimalFormat sciFormat = new DecimalFormat("0.00E0");
    private DecimalFormat tmpsciFormat = new DecimalFormat("###0.0000#####################");
    String defaultV = "--";

    public LevelOfExpressionIHCFindingReportBean() {}
    
    public LevelOfExpressionIHCFindingReportBean(LevelOfExpressionIHCFinding finding){
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
        //return null;
       else return defaultV;
    }
    
    public String getBiomarkerName(){
        if(finding.getProteinBiomarker().getName()!=null){
            return finding.getProteinBiomarker().getName();
        }
        else return defaultV;        
    }
    
    public String getOverallExpression() {
        if(finding.getOverallExpression()!=null){
            return finding.getOverallExpression();
        }
        else return defaultV;       
    }
    
    public String getPercentPositive(){
        String percentPositive = defaultV;
        if(finding.getPercentPositiveRangeMin()!=null && finding.getPercentPositiveRangeMax()!=null){
            if(!finding.getPercentPositiveRangeMax().equals(finding.getPercentPositiveRangeMin())){           
                percentPositive = finding.getPercentPositiveRangeMin().toString();
                percentPositive += "-";
                percentPositive += finding.getPercentPositiveRangeMax().toString();                
            }
            else percentPositive = finding.getPercentPositiveRangeMin().toString();
        }
////        else if(finding.getPercentPositive()!=null){
////            percentPositive = finding.getPercentPositive().toString();
////        }
        return percentPositive;
    }
    
    public String getStainDistribution() {
        if(finding.getStainDistribution()!=null){
            return finding.getStainDistribution();
        }
        else return defaultV;
    }
    
    public String getStainIntensity() {
        if(finding.getStainIntensity()!=null){
            return finding.getStainIntensity();
        }
        else return defaultV;
    }
    
    public String getStainLocalization() {
        if(finding.getStainLocalization()!=null){
            return finding.getStainLocalization();
        }
        else return defaultV;
    }
    
    public String getInvasivePresentation() {
        if(finding.getInvasivePresentation()!=null){
            return finding.getInvasivePresentation();
        }
        else return defaultV;
    }

    public ArrayList<String> getHeaders(){
        String[] myHeaders = {"% Positive", "Intensity", "Localization","Result Code", "Summary"};        
        ArrayList<String> headers = new ArrayList<String>(Arrays.asList(myHeaders));
        return headers;
    }
    
    public ArrayList<String> getNonTimepointHeaders(){
        String[] myHeaders = {"PatientDID", "Biomarker"};        
        ArrayList<String> headers = new ArrayList<String>(Arrays.asList(myHeaders));
        return headers;
    }
    
    public ArrayList<String> getTimepointHeaders(LevelOfExpressionIHCFindingCriteria criteria){
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
