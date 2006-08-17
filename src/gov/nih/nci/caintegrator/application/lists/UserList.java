/**
 * The UserList will define an uploaded list by name, the type of list and the 
 * contained list itself.
 */
package gov.nih.nci.caintegrator.application.lists;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author rossok
 *
 */
public class UserList { 
    private String name = "";
    private ListType listType;
    //a list can actually have several sub-types
    private List<ListSubType> listSubTypes;
    //user attached notes for a list
    private String notes;
    private List<String> list = new ArrayList<String>();
    private List<String> invalidList = new ArrayList<String>();
    private Date dateCreated;
    private int itemCount = 0;
   
    public UserList(){}
    
    //base constructor: no subtype, no notes
    public UserList(String name, ListType listType, List<String> list, List<String> invalidList, Date dateCreated){
        this.name = name;
        this.listType = listType;
        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
        this.list = list;
        this.invalidList = invalidList;
        this.dateCreated = dateCreated;
        this.itemCount = list.size();
    }
    
    //full constructor
    public UserList(String name, ListType listType, List<ListSubType> listSubTypes, List<String> list, List<String> invalidList, Date dateCreated, String notes)	{
        new UserList(name, listType, listSubTypes, list, invalidList, dateCreated);
        this.notes = notes;        
    }
    
    //Base constructor plus listSubType
    public UserList(String name, ListType listType, List<ListSubType> listSubTypes, List<String> list, List<String> invalidList, Date dateCreated)	{
        new UserList(name,listType,list,invalidList,dateCreated);
        this.listSubTypes = listSubTypes;        
    }

    
    /**
     * @return Returns the dateCreated.
     */
    public Date getDateCreated() {
        return dateCreated;
    }
    /**
     * @param dateCreated The dateCreated to set.
     * @throws ParseException 
     */
    public void setDateCreated(Date dateCreated) throws ParseException {
        this.dateCreated = dateCreated;
    }
    /**
     * @return Returns the itemCount.
     */
    public int getItemCount() {
        return itemCount;
    }
    /**
     * @param itemCount The itemCount to set.
     */
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
    /**
     * @return Returns the list.
     */
    public List<String> getList() {
        return list;
    }
    /**
     * @param list The list to set.
     */
    public void setList(List<String> list) {
        this.list = list;
    }
    /**
     * @return Returns the listType.
     */
    public ListType getListType() {
        return listType;
    }
    /**
     * @param listType The listType to set.
     */
    public void setListType(ListType listType) {
        this.listType = listType;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    public String toString() {
        return name;
    }
    /**
     * @return Returns the invalidList.
     */
    public List<String> getInvalidList() {
        return invalidList;
    }
    /**
     * @param invalidList The invalidList to set.
     */
    public void setInvalidList(List<String> invalidList) {
        this.invalidList = invalidList;
    }

	public List<ListSubType> getListSubType() {
		return listSubTypes;
	}

	public void setListSubType(List<ListSubType> listSubTypes) {
		this.listSubTypes = listSubTypes;
	}
	
	//conv. in case you only want to add one
	public void setListSubType(ListSubType listSubType){
		//if(this.listSubTypes.isEmpty())	{
			ArrayList<ListSubType> st = new ArrayList();
			st.add(listSubType);
			setListSubType(st);
	//	}
	//	else if(!this.listSubTypes.contains(listSubType))	{
	//		this.listSubTypes.add(listSubType);
	//	}
	}
	
	public boolean hasSubType(ListSubType listSubType)	{
		if(listSubTypes.contains(listSubType))	{
			return true;
		}
		return false;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
