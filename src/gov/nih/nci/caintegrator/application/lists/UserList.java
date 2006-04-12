/**
 * The UserList will define an uploaded list by name, the type of list and the 
 * contained list itself.
 */
package gov.nih.nci.caintegrator.application.lists;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rossok
 *
 */
public class UserList { 
    String name = "";
    ListType listType;
    List<String> list = new ArrayList<String>();
    
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
