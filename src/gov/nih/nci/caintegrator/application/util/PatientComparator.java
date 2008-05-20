package gov.nih.nci.caintegrator.application.util;

import gov.nih.nci.caintegrator.application.bean.LevelOfExpressionIHCFindingReportBean;

import java.util.Comparator;

public class PatientComparator implements Comparator<LevelOfExpressionIHCFindingReportBean>{
    public PatientComparator(){}
    
    public int compare(LevelOfExpressionIHCFindingReportBean bean1, LevelOfExpressionIHCFindingReportBean bean2) {
        if ((bean1 == null) || (bean2 == null)) {
            return 0;
        }
        
        return (bean2.getPatientDID().compareTo(bean1.getPatientDID()));
    }

}
