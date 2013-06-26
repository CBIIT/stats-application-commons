/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.workspace;

import gov.nih.nci.caintegrator.application.lists.UserList;

import java.util.List;
import java.util.ArrayList;
import java.lang.Iterable;
import java.util.Iterator;


/**
 * @author prakasht
 * This model is used for managing the workspace during Import/Export
 */
public class WorkspaceList extends UserList implements Iterable {
	private List<UserList> folderList = new ArrayList<UserList>();
	
	public WorkspaceList()
	{
		super();
	}
	
	public WorkspaceList( String name )
	{
		super();
		setName( name );
	}
	
	public void addLeaf( UserList list )
	{
		folderList.add( list );
	}

	public List<UserList> getFolderList() {
		return folderList;
	}
	
	public Iterator<UserList> iterator()
	{
		return folderList.iterator();
	}

}
