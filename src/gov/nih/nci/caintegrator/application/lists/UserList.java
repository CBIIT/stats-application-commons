/**
 * The UserList will define an uploaded list by name, the type of list and the 
 * contained list itself.
 */
package gov.nih.nci.caintegrator.application.lists;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author rossok
 *
 */
public class UserList { 
    private String name = "";
    private ListType listType;
    private List<String> list = new ArrayList<String>();
    private Date dateCreated;
    private int itemCount = 0;
   
    
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
}
