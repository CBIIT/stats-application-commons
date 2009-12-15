package gov.nih.nci.caintegrator.security;


import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;

import java.io.Serializable;
import java.util.Collection;

/***
 * Class that will hold the "Credentials" required to fully use a caIntegrator
 * based application.  It will hold the Role of the user and the protectionElements that
 * the user is associated with.
 * 
 * The UserCredentials may have 1 of 3 roles.
 * 	
 *  UserRole.PUBLIC_USER is able to access only public data
 *  
 *  UserRole.INSTITUTE_USER is able to view PUBLIC_USER data and the data of the Institutes
 *  listed in the Set protectionElements.
 *  
 *  UserRole.SUPER_USER is able to view all data across all protectionElements as well
 *  as all public data
 * 
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

public class UserCredentials implements Serializable{

	private Long userId;
	private String userName;
	private UserRole role;
	private String emailAddress;
	private String firstName;
	private String lastName;
	private Collection<ProtectionElement> protectionElements;
	private boolean authenticated = false;
	
	public enum UserRole{
		PUBLIC_USER, INSTITUTE_USER, SUPER_USER;
		public  String toString()
		{
			switch(this) {
			case PUBLIC_USER:
				return "PUBLIC_USER";
			case INSTITUTE_USER:
				return "INSTITUTE_USER";
			case SUPER_USER:
				return "SUPER_USER";
			default:
				//this should never happen
				return "UNDEFINED_USER_ROLE";
			}
		}	
	}

	/**
	 * This constructor is protected so that once the Credentials have been
	 * set by the SecurityManager, they can not be modified.
	 * @param emailAddress
	 * @param firstName
	 * @param protectionElements
	 * @param lastName
	 * @param role
	 * @param userName
	 */
	protected UserCredentials(Long userId, String emailAddress, String firstName, Collection<ProtectionElement> protectionElements, String lastName, UserRole role, String userName) {
		this.userId = userId;
		this.emailAddress = emailAddress;
		this.firstName = firstName;
		this.protectionElements = protectionElements;
		this.lastName = lastName;
		this.role = role;
		this.userName = userName;
		if(role!=null) {
			authenticated = true;
		}
	}
	/**
	 * This constructor is protected so that once the Credentials have been
	 * set by the SecurityManager, they can not be modified.
	 * @param userName
	 * @param role
	 * @param protectionElements
	 */
	protected UserCredentials(String userName, UserRole role, Collection<ProtectionElement> allowableData) {
		this.userName = userName;
		this.role = role;
		this.protectionElements = allowableData;
		if(role!=null) {
			authenticated = true;
		}
	}
	/**
	 * The protectionElements whose data the user is allowed to see
	 * @return
	 */
	public Collection<ProtectionElement> getprotectionElements() {
		return this.protectionElements;
	}

	/**
	 * @return Returns the role.
	 */
	public UserRole getRole() {
		return role;
	}

	/**
	 * @return Returns the userName.
	 */
	public String getUserName() {
		return userName;
	}


	
	/**
	 * Checks to see if these credential have been authenticated
	 * @return
	 */
	public boolean authenticated() {
		return authenticated;
	}
	
	private void setAuthenticated(boolean auth) {
		this.authenticated = auth;
	}

	/**
	 * @param emailAddress The emailAddress to set.
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @param firstName The firstName to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @param lastName The lastName to set.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	

}