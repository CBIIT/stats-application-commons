/**
 * The UserList will define an uploaded list by name, the type of list and the 
 * contained list itself.
 */
package gov.nih.nci.caintegrator.application.lists;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author rossok
 *
 */
public class UserList implements Serializable{ 
    private Long id;
    private String name = "";
    private ListType listType;
    //a list can actually have several sub-types
    private List<ListSubType> listSubType;
   
    //user attached notes for a list
    private String notes;
    //private List<String> list = new ArrayList<String>();
    //private List<String> invalidList = new ArrayList<String>();
    private List<ListItem> list = new ArrayList<ListItem>();
    private List<ListItem> listItems = new ArrayList<ListItem>();
    private List<ListItem> invalidList = new ArrayList<ListItem>();
    private List<ListItem> invalidListItems = new ArrayList<ListItem>();
    private Date dateCreated;
    private int itemCount = 0;
    private String author;
    private String institute;
    private String category;
    private boolean isDefault;
    
    
    /**
     * @return Returns the isDefault.
     */
    public boolean isDefault() {
        return isDefault;
    }

    /**
     * @param isDefault The isDefault to set.
     */
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    //newest constructor to replace string listsd with listitem lists
    public UserList(String name, ListType listType, List<ListItem> list, List<ListItem> invalidList){
        this.name = name;
        this.listType = listType;
        this.list = list;
        this.invalidList = invalidList;        
    }
        
    /**
     * @deprecated
     */
    public UserList(){}
    
    //base constructor: no subtype, no notes
    /**
     * @deprecated
     */
    public UserList(String name, ListType listType, List<String> list, List<String> invalidList, Date dateCreated){
        this.name = name;
        this.listType = listType;
        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
        for(String string: list){
            this.list.add(new ListItem(string,name));
        }
        for(String invalidString: invalidList){
            this.invalidList.add(new ListItem(invalidString));
        }        
        this.dateCreated = dateCreated;
        this.itemCount = this.list.size();        
    }
    
    //full constructor
    /**
     * @deprecated
     */
    public UserList(String name, ListType listType, List<ListSubType> listSubTypes, List<String> list, List<String> invalidList, Date dateCreated, String notes)    {
        new UserList(name, listType, listSubTypes, list, invalidList, dateCreated);
        this.notes = notes;        
    }
    
    //Base constructor plus listSubType
    /**
     * @deprecated
     */
    public UserList(String name, ListType listType, List<ListSubType> listSubTypes, List<String> list, List<String> invalidList, Date dateCreated)  {
        new UserList(name,listType,list,invalidList,dateCreated);
        this.listSubType = listSubTypes;        
    }

    
   
    
    /**
     * @return Returns the author.
     */
    public String getAuthor() {
        return author;
    }
    /**
     * @param author The author to set.
     */
    public void setAuthor(String author) {
        this.author = author;
    }
    /**
     * @return Returns the category.
     */
    public String getCategory() {
        return category;
    }
    /**
     * @param category The category to set.
     */
    public void setCategory(String category) {
        this.category = category;
    }
    /**
     * @return Returns the institute.
     */
    public String getInstitute() {
        return institute;
    }
    /**
     * @param institute The institute to set.
     */
    public void setInstitute(String institute) {
        this.institute = institute;
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
        return list.size();
    }
    /**
     * @param itemCount The itemCount to set.
     */
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
    /**
     * returns the list as ListItems
     * TODO (see setListItems) this wille eventually take the place of getList()
     * Currently it serves to return the old field (list) but will be phased in
     * on a per app basis.
     * 
     */
    public List<ListItem> getListItems(){
        return this.list;
    }
    
    /**
     * return the invalidList as ListItems
     * TODO SEE setListItems() same rules will apply. This is temporary
    */
    public List<ListItem> getInvalidListItems(){
        return this.invalidList;
    }
    
    /**
     * @deprecated
     * @return Returns the list (AS STRINGS).
     */
    public List<String> getList() {
        List<String> stringList = new ArrayList<String>();
        for(ListItem item: list){
            stringList.add(item.getName());
        }
        return stringList;
    }
    /**
     * @deprecated
     * @param list The list to set.
     */
    public void setList(List<String> list) {
        for(String s: list){
            this.list.add(new ListItem(s));
        }        
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
     * @deprecated
     * @return Returns the invalidList (AS STRINGS).
     */
    public List<String> getInvalidList() {
        List<String> stringList = new ArrayList<String>();
        for(ListItem item: invalidList){
            stringList.add(item.getName());
        }
        return stringList;
    }
    /**
     * @deprecated
     * @param invalidList The invalidList to set.
     */
    public void setInvalidList(List<String> invalidList) {
        for(String s: invalidList){
            this.invalidList.add(new ListItem(s));
        } 
    }

    public List<ListSubType> getListSubType() {
        return listSubType;
    }

    
    //conv. in case you only want to add one
    public void setListSubType(ListSubType listSubType){
        //if(this.listSubTypes.isEmpty())   {
            ArrayList<ListSubType> st = new ArrayList();
            st.add(listSubType);
            setListSubType(st);
    //  }
    //  else if(!this.listSubTypes.contains(listSubType))   {
    //      this.listSubTypes.add(listSubType);
    //  }
    }
    
    public void setListSubType(List<ListSubType> listSubType) {
        this.listSubType = listSubType;
    }
    
    public boolean hasSubType(ListSubType listSubType)  {
        if(this.listSubType.contains(listSubType))  {
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

    /**
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @param listItems The listItems to set.
     * TODO this needs to replace list field eventually. Right now it serves as
     * the 'proper way to set the listItems although most current apps are not using it.
     * The job right now for apps that do use it is to simply
     * assign it to the current field 'list'.
     */
    public void setListItems(List<ListItem> listItems) {
        this.list = listItems;
    }

    /**
     * @param invalidListItems The invalidListItems to set.
     * TODO see setListItems(). same rules apply, This is
     * temporary.
     */
    public void setInvalidListItems(List<ListItem> invalidListItems) {
        this.invalidList = invalidListItems;
    }

    
}
