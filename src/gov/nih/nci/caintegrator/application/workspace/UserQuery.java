/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.workspace;

/**
 * @author sahnih
 *
 */
public class UserQuery implements java.io.Serializable{
	//UserQuery Id 
	private Long id;
	//User Id
	private Long userId;
	//Query Content
	private byte[] queryContent;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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
	/**
	 * @return the queryContent
	 */
	public byte[] getQueryContent() {
		return queryContent;
	}
	/**
	 * @param queryContent the queryContent to set
	 */
	public void setQueryContent(byte[] queryContent) {
		this.queryContent = queryContent;
	}
}
