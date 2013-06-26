/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.security;

import gov.nih.nci.caintegrator.application.security.UserInfoBean;
import gov.nih.nci.caintegrator.application.security.LoginException;
import gov.nih.nci.caintegrator.security.UserCredentials;

/**
 * This is an interface for the LoginService.  It has one method
 * that throws an Exception if the user is not authenticated, or 
 * returns a UserInfoBean if they are.
 * 
 *
 * @author caIntegrator Team
 */
public interface LoginService {
    
    /**@deprecated
     * 
     * @param username
     * @param password
     * @return
     * @throws LoginException
     */
    public UserInfoBean loginUser(String username, String password) throws LoginException;
    
    public UserCredentials login(String username, String password) throws LoginException;

    public void setAPP_NAME(String name);
    
    public String getAPP_NAME();
}
