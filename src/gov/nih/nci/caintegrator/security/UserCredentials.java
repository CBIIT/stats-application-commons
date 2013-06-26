/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

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
