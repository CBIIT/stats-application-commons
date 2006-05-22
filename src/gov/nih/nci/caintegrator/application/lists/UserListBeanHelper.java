/**
 * This helper class access the userListBean in the session. 
 */
package gov.nih.nci.caintegrator.application.lists;

import java.util.List;

import org.w3c.dom.Document;



/**
 * @author rossok
 *
 */
public interface UserListBeanHelper {

    public void addList(UserList userList);
    
    public void removeList(String listName);
    
    public void addItemToList(String listName, String listItem);
    
    public void removeItemFromList(String listName, String listItem);
    
    public Object getDetailsFromList(String listName);
    
    public List<UserList> getLists(ListType listType);
    
    public List<UserList> getAllLists();
    
    public UserList getUserList(String listName);
    
    public List getItemsFromList(String listName);
    
    public void uniteLists(List<String> listNames, String newListName, ListType listType);
    
    public void intersectLists(List<String> listNames, String newListName, ListType listType);

}
