/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.security;

import javax.security.sasl.AuthenticationException;

import org.apache.log4j.Logger;

import gov.nih.nci.caintegrator.security.UserCredentials;
import gov.nih.nci.caintegrator.security.SecurityManager;
import gov.nih.nci.caintegrator.application.security.UserInfoBean;
import gov.nih.nci.caintegrator.application.security.LoginException;


/**
 * The CsmLoginService logs a user in using the Common Security
 * Module.
 * 
 *
 * @author caIntegrator Team
 */
public class CsmLoginService implements LoginService {

    private String APP_NAME = "";
    private static Logger logger = Logger.getLogger(CsmLoginService.class);

    /**
     * This method will authenticate a user agains the security manager
     * for the specified project.  Throws a LoginException if the user
     * cannot be authenticated, otherwise returns a UserInfoBean with information
     * about the user.
     *
     * @param userName
     * @param password
     * @return
     * @throws LoginException
     * @deprecated
     */
    public UserInfoBean loginUser(String userName, String password)
            throws LoginException {

        UserInfoBean userInfo = new UserInfoBean();

        UserCredentials user = null;
        SecurityManager secManager = SecurityManager.getInstance(APP_NAME);
        try {
            if (secManager.authenticate(userName, password)) {
                user = secManager.authorization(userName);
            }
        } catch (AuthenticationException e) {
            throw new LoginException(e);
        }
        userInfo.setLoggedIn(true);
        userInfo.setUserName(userName);
        

        return userInfo;
    }
    
    /**
     * This method will authenticate a user agains the security manager
     * for the specified project.  Throws a LoginException if the user
     * cannot be authenticated, otherwise returns a UserInfoBean with information
     * about the user.
     *
     * @param userName
     * @param password
     * @return
     * @throws LoginException
     */
    public UserCredentials login(String userName, String password)
            throws LoginException {

       UserCredentials user = null;
        SecurityManager secManager = SecurityManager.getInstance(APP_NAME);
        try {
            if (secManager.authenticate(userName, password)) {
                user = secManager.authorization(userName);
            }
        } catch (AuthenticationException e) {
            throw new LoginException(e);
        }

        return user;
    }
    

    /**
     * @return Returns the aPP_NAME.
     */
    public String getAPP_NAME() {
        return APP_NAME;
    }

    /**
     * @param app_name The aPP_NAME to set.
     */
    public void setAPP_NAME(String app_name) {
        APP_NAME = app_name;
    }

}
