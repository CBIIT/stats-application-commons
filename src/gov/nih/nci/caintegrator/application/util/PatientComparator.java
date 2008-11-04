package gov.nih.nci.caintegrator.application.util;

import gov.nih.nci.caintegrator.application.bean.IHCFindingReportBean;

import java.util.Comparator;

public class PatientComparator implements Comparator<IHCFindingReportBean>{
    public PatientComparator(){}
    
    public int compare(IHCFindingReportBean bean1, IHCFindingReportBean bean2) {
        if ((bean1 == null) || (bean2 == null)) {
            return 0;
        }
        
        return (bean2.getPatientDID().compareTo(bean1.getPatientDID()));
    }

}
