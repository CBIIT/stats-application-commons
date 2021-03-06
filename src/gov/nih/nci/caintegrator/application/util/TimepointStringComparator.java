/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.util;

import java.util.Comparator;

public class TimepointStringComparator implements Comparator<String>{
    public TimepointStringComparator(){}
   
    
    public int compare(String bean1, String bean2) {
        if ((bean1 == null) || (bean2 == null)) {
            return 0;
        }
        return (bean1.compareTo(bean2));
    }

}
