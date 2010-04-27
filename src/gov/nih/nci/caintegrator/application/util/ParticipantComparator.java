package gov.nih.nci.caintegrator.application.util;

import gov.nih.nci.caintegrator.domain.finding.clinical.bean.ClinicalFinding;

import java.util.Comparator;

public class ParticipantComparator implements Comparator<ClinicalFinding>{
    public ParticipantComparator(){}
    
    public int compare(ClinicalFinding bean1, ClinicalFinding bean2) {
        if ((bean1 == null) || (bean2 == null)) {
            return 0;
        }
        
        return (bean1.getStudyParticipant().getId().compareTo(bean2.getStudyParticipant().getId()));
    }

}
