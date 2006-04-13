/**
 * The userlist bean contains all lists created by the user and has session
 * scope. It is accessed by the UserListBean Helper
 */
package gov.nih.nci.caintegrator.application.lists;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rossok
 *
 */
public class UserListBean {

    List<UserList> userLists = new ArrayList<UserList>();
    
    public void addList(UserList userList){
        userLists.add(userList);
    }
    
    public void removeList(String listName){
        userLists.remove(listName);
    }
    
    public List<UserList> getEntireList(){
        return userLists;
    }
    
    public UserList getList(String listName){
        for(UserList list : userLists){
            if(list.getName().equals(listName)){
                return list;
            }            
        }
        return null;
        
    }

}
