/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

/**
 * 
 */
package gov.nih.nci.caintegrator.application.workspace;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Clob;
import java.sql.SQLException;

import org.hibernate.Hibernate;

/**
 * @author sahnih
 *
 */
public class Workspace {
	//Workspace Id 
	private Long id;
	//User Id
	private Long userId;
	//Tree Type: QUERY or LIST
	private String treeType;
	//Tree Structure
	private String treeStructure;
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
	 * @return the treeType
	 */
	public String getTreeType() {
		return treeType;
	}
	/**
	 * @param treeType the treeType to set
	 */
	public void setTreeType(String treeType) {
		this.treeType = treeType;
	}
	/**
	 * @return the treeStructure
	 */
	public String getTreeStructure() {
		return treeStructure;
	}
	/**
	 * @param treeStructure the treeStructure to set
	 */
	public void setTreeStructure(String treeStructure) {
		this.treeStructure = treeStructure;
	}

	
}
