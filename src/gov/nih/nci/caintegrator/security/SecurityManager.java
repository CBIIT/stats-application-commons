package gov.nih.nci.caintegrator.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import gov.nih.nci.caintegrator.dto.de.InstitutionDE;
import gov.nih.nci.caintegrator.security.UserCredentials.UserRole;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElementPrivilegeContext;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.exceptions.CSException;

import javax.security.sasl.AuthenticationException;

import org.apache.log4j.Logger;
/**
 * This will eventually become an implementation class of the security manager
 * interface that will be bundled with caIntegrator.  For now it will reside in
 * caIntegator_spec as we work on getting everything in the security module
 * ironed out.
 * 
 * @author BauerD
 *
 */


/**
* caIntegrator License
* 
* Copyright 2001-2005 Science Applications International Corporation ("SAIC"). 
* The software subject to this notice and license includes both human readable source code form and machine readable, 
* binary, object code form ("the caIntegrator Software"). The caIntegrator Software was developed in conjunction with 
* the National Cancer Institute ("NCI") by NCI employees and employees of SAIC. 
* To the extent government employees are authors, any rights in such works shall be subject to Title 17 of the United States
* Code, section 105. 
* This caIntegrator Software License (the "License") is between NCI and You. "You (or "Your") shall mean a person or an 
* entity, and all other entities that control, are controlled by, or are under common control with the entity. "Control" 
* for purposes of this definition means (i) the direct or indirect power to cause the direction or management of such entity,
*  whether by contract or otherwise, or (ii) ownership of fifty percent (50%) or more of the outstanding shares, or (iii) 
* beneficial ownership of such entity. 
* This License is granted provided that You agree to the conditions described below. NCI grants You a non-exclusive, 
* worldwide, perpetual, fully-paid-up, no-charge, irrevocable, transferable and royalty-free right and license in its rights 
* in the caIntegrator Software to (i) use, install, access, operate, execute, copy, modify, translate, market, publicly 
* display, publicly perform, and prepare derivative works of the caIntegrator Software; (ii) distribute and have distributed 
* to and by third parties the caIntegrator Software and any modifications and derivative works thereof; 
* and (iii) sublicense the foregoing rights set out in (i) and (ii) to third parties, including the right to license such 
* rights to further third parties. For sake of clarity, and not by way of limitation, NCI shall have no right of accounting
* or right of payment from You or Your sublicensees for the rights granted under this License. This License is granted at no
* charge to You. 
* 1. Your redistributions of the source code for the Software must retain the above copyright notice, this list of conditions
*    and the disclaimer and limitation of liability of Article 6, below. Your redistributions in object code form must reproduce 
*    the above copyright notice, this list of conditions and the disclaimer of Article 6 in the documentation and/or other materials
*    provided with the distribution, if any. 
* 2. Your end-user documentation included with the redistribution, if any, must include the following acknowledgment: "This 
*    product includes software developed by SAIC and the National Cancer Institute." If You do not include such end-user 
*    documentation, You shall include this acknowledgment in the Software itself, wherever such third-party acknowledgments 
*    normally appear.
* 3. You may not use the names "The National Cancer Institute", "NCI" "Science Applications International Corporation" and 
*    "SAIC" to endorse or promote products derived from this Software. This License does not authorize You to use any 
*    trademarks, service marks, trade names, logos or product names of either NCI or SAIC, except as required to comply with
*    the terms of this License. 
* 4. For sake of clarity, and not by way of limitation, You may incorporate this Software into Your proprietary programs and 
*    into any third party proprietary programs. However, if You incorporate the Software into third party proprietary 
*    programs, You agree that You are solely responsible for obtaining any permission from such third parties required to 
*    incorporate the Software into such third party proprietary programs and for informing Your sublicensees, including 
*    without limitation Your end-users, of their obligation to secure any required permissions from such third parties 
*    before incorporating the Software into such third party proprietary software programs. In the event that You fail 
*    to obtain such permissions, You agree to indemnify NCI for any claims against NCI by such third parties, except to 
*    the extent prohibited by law, resulting from Your failure to obtain such permissions. 
* 5. For sake of clarity, and not by way of limitation, You may add Your own copyright statement to Your modifications and 
*    to the derivative works, and You may provide additional or different license terms and conditions in Your sublicenses 
*    of modifications of the Software, or any derivative works of the Software as a whole, provided Your use, reproduction, 
*    and distribution of the Work otherwise complies with the conditions stated in this License.
* 6. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, 
*    THE IMPLIED WARRANTIES OF MERCHANTABILITY, NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. 
*    IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE, SAIC, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, 
*    INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
*    GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
*    LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT 
*    OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
* 
*/

public class SecurityManager {
	public static final String USER_NOT_FOUND_IN_AUTHENTICATION_DB = "USER NOT FOUND_IN_AUTHENTICATION_DB";
	private static SecurityManager instance;
	private static Logger logger = Logger.getLogger(SecurityManager.class);
	private static AuthorizationManager authorizationManager;
	private static UserProvisioningManager userProvisioningManager;
	private static String application = "caIntegrator" ;
	
	private SecurityManager(){}
	private SecurityManager(String app){application = app ;}
	public static SecurityManager getInstance(){
		if( instance == null){
			instance = new SecurityManager();
		}
		return instance;
	}
	
	public static SecurityManager getInstance(String app){
		if( instance == null){
			instance = new SecurityManager(app);
		}
		return instance;
	}
	/**
	 * 	
	 * This method Will authenticate the user by the password they provide. Will either 
	 *  return true, false or will throw an
	 * AuthenticationException.
	 * 
	 * This implementation is currently using CSM via LDAP to authenticate.
	 * Authorization is done through the CSM tables with the application database 
	 * @param userName
	 * @param password
	 * @return
	 * @throws AuthenticationException
	 */
	public boolean authenticate(String userName, String password) throws AuthenticationException{
		boolean authenticated;
		try {
		 authenticated = localAuthenticate(userName,password);
		}catch(AuthenticationException ae) {
				throw new AuthenticationException(ae.getMessage());
		}
		return authenticated;
	}
	/***
	 * Will authorize the user by the password they provide. Will either 
	 * create and return a populated UserCredentials object or will throw an
	 * AuthenticationException.
	 * 
	 * Authorization is done through the Rembrandt database where user roles
	 * are stored with associated Institutes.
	 * 
	 * @param userName
	 * @param password
	 * @return userCredentials can be null if any exceptions are thrown while
	 * trying to access the SecurityManager classes.
	 *  
	 * @throws AuthenticationException
	 */
	@SuppressWarnings("unchecked")
	public UserCredentials authorization(String userName) throws AuthenticationException{
		UserCredentials credentials = null;
		try {
			authorizationManager = getAuthorizationManager();
			userProvisioningManager = getUserProvisioningManager();
		}catch(Exception e) {
			logger.error(e);
		}
		Collection<InstitutionDE> institutes = new ArrayList<InstitutionDE>();
		User user = authorizationManager.getUser(userName);
		if(user == null){
			logger.warn("User "+ userName +" not found") ;
			throw new AuthenticationException(USER_NOT_FOUND_IN_AUTHENTICATION_DB);
		}
		/**
		 * Protection Elements are the data sets that each institute has submitted
		 * Each user is asigned first a username, then a group.  A group is assigned
		 * a protection group, and a protection group is assigned a set of protection
		 * elements.
		 * 
		 * For instance danielR is assigned to the SUPER_USER group.  The SUPER_USER
		 * group has a Protection Group called SUPER_USER_DATA.  SUPER_USER_DATA
		 * has all the applications Protection Elements assigned to it.  In other
		 * words username danielR is able to access all protection elements in
		 * the application, or rather, danielR is able to access all submitted
		 * data from all institutes.
		 * 
		 * IMPORTANT!
		 * For caIntegrator we refer to what the CSM calls a group as a Role.
		 * This may change later but I thought I would put that down here so 
		 * as to avoid confusion.  So, we will be grabbing the assigned group(s)
		 * for the user and then assigning them a "Role" in the application
		 * 
		 * DBauer
		 */
		Set protectionElements;
		String emailAddress = null;
		String firstName = null;
		String lastName = null;
		Set<Group> groups;
		try {
			emailAddress = user.getEmailId();
			firstName = user.getFirstName();
			lastName = user.getLastName();
			protectionElements = userProvisioningManager.getProtectionElementPrivilegeContextForUser(user.getUserId().toString());
			groups = userProvisioningManager.getGroups(user.getUserId().toString());	
		} catch (Exception e) {
			logger.error("No ProtectionElementPrivlegeContexts found for user:"+ userName);
			logger.error(e);
			throw new AuthenticationException("ProtectionElementPrivlegeContexts are null");
		}
		if(protectionElements!=null) {
			try {
				/*
				 * For all the protection elements allowed for reading
				 * create institution domain elements and store into the
				 * user credentials.
				 */
				for(Iterator i = protectionElements.iterator();i.hasNext();) {
					ProtectionElementPrivilegeContext pepc = (ProtectionElementPrivilegeContext)i.next();
					ProtectionElement pe = pepc.getProtectionElement();
					Long instituteId = new Long(pe.getObjectId());
					String name = pe.getProtectionElementName();
					institutes.add(new InstitutionDE(name,instituteId));
				}
				
				logger.debug("Username: "+userName+" has the following credentials:");
				logger.debug("--------------------------------------------------------");
				for(InstitutionDE institute:institutes) {
					logger.debug("Allowed to read:" +institute.getInstituteName());
				}
				logger.debug("--------------------------------------------------------");
				/*
				 * Create the UserRole for the application.  Right now
				 * this isn't really used for anything as the actual
				 * credentials contain the information that will be used to
				 * determine what the user is allowed to actually see.  But
				 * we will pass it along for now incase we want to make any
				 * snap judgments about the user after they are logged in
				 */
				UserRole role = getUserRole(groups);
				credentials = new UserCredentials(emailAddress,firstName,institutes,lastName,role,userName);
			}catch(NullPointerException npe) {
				logger.error("Security Objects are null.") ;
				throw new AuthenticationException("Some SecurityObjects are null");
			}
		}
		
		if(credentials==null){
			logger.error("authentication is returning a Null Credentials Object");
		}
		return credentials;
	}
	/**
	 * This method creates a UserRole for the application based on the the group
	 * that the userId is assigned.  There are three possibilities:
	 * 	SUPER_USER - Allowed to see all data
	 * 	PUBLIC_USER -Only allowed to see public data
	 * 	INSTITUTE_USER - Only allowed to see public data and data that insititute
	 * 	has submitted
	 * 
	 * @param groups
	 * @return
	 */
	private UserRole getUserRole(Set<Group> groups) {
		UserRole role = null;
		for(Group group: groups) {
			String groupName = group.getGroupName();
			if(groupName.equals(UserRole.SUPER_USER.toString())) {
				role = UserRole.SUPER_USER;
			}else if(groupName.equals(UserRole.PUBLIC_USER.toString())) {
				role = UserRole.PUBLIC_USER;
			}else {
				role = UserRole.INSTITUTE_USER;
			}
		}
		logger.debug("User assigned the role of "+role);
		return role;
	}

	/**
	 * Authenticates the username and password using the Common Security Module
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	private boolean localAuthenticate(String username, String password) throws AuthenticationException{
		 AuthenticationManager am = null;
		 
	        boolean loggedIn = false;
	        try {
	            /**
	    		 * TODO get application Ccontext from system properties
	    		 */
	            am = SecurityServiceProvider.getAuthenticationManager(application);
	            loggedIn = am.login(username, password);

	        } catch (CSException e) {
	            throw new AuthenticationException("User Name or Password is not correct");		            
	        }
	        
	        return loggedIn;
	}
	private static AuthorizationManager getAuthorizationManager() throws CSException{
		if(authorizationManager==null) {
			authorizationManager = SecurityServiceProvider.getAuthorizationManager(application);
		}
		return authorizationManager;
	}	
	private User getUser(String loginName){
		if(authorizationManager!=null) {
			return authorizationManager.getUser(loginName);
		}else {
			return null;
		}
	}	
	private static UserProvisioningManager getUserProvisioningManager() throws CSException{
		if(userProvisioningManager==null) {
			userProvisioningManager = SecurityServiceProvider.getUserProvisioningManager(application +
					"");
		}
		return userProvisioningManager;
	}	
}
