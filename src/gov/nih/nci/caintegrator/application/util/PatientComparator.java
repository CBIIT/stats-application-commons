/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.util;

import gov.nih.nci.caintegrator.application.bean.IHCFindingReportBean;
import gov.nih.nci.caintegrator.application.bean.P53FindingReportBean;

import java.util.Comparator;

/*public class PatientComparator implements Comparator<IHCFindingReportBean>{
    public PatientComparator(){}
    
    public int compare(IHCFindingReportBean bean1, IHCFindingReportBean bean2) {
        if ((bean1 == null) || (bean2 == null)) {
            return 0;
        }
        
        return (bean2.getPatientDID().compareTo(bean1.getPatientDID()));
    }

}*/



public class PatientComparator implements Comparator{
    public PatientComparator(){}
    
   
    
    public int compare(Object bean1, Object bean2) {
    	
        if ((bean1 == null) || (bean2 == null)) {
            return 0;
        }
        
        else if(bean1 instanceof IHCFindingReportBean && bean2 instanceof IHCFindingReportBean ){
            return (((IHCFindingReportBean)bean2).getPatientDID().compareTo(((IHCFindingReportBean)bean1).getPatientDID()));
        }
        
        else if(bean1 instanceof P53FindingReportBean && bean2 instanceof P53FindingReportBean ){
            return (((P53FindingReportBean)bean2).getPatientDID().compareTo(((P53FindingReportBean)bean1).getPatientDID()));
        }
        
        else 
        	return 0;
    }
    
    

}
