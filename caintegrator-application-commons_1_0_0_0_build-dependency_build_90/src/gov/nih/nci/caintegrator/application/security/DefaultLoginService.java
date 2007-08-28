package gov.nih.nci.caintegrator.application.security;

import org.apache.log4j.Logger;


/**
 * The CsmLoginService logs a user in using the Common Security
 * Module.
 * 
 *
 * @author caIntegrator Team
 */
public class DefaultLoginService implements LoginService {

    private String APP_NAME = "";
    private static Logger logger = Logger.getLogger(DefaultLoginService.class);

    /**
     * This method will not authenticate a user and should be implemented 
     * by the client. A csm login service is also available and, if CSM login
     * service is to be used the service can be swapped in applicationContext-services file
     *
     * @param userName
     * @param password
     * @return
     * @throws LoginException
     */
    public UserInfoBean loginUser(String userName, String password)
            throws LoginException {

        UserInfoBean userInfo = new UserInfoBean();
        
        userInfo.setLoggedIn(true);
        userInfo.setUserName(userName);

        return userInfo;
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
