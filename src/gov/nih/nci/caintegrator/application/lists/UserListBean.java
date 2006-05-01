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
        for(UserList list: userLists){        
            if(list.getName().equals(userList.getName())){
            userLists.remove(list);
            break;
           }
         }        
        userLists.add(userList);
    }
    
    public void removeList(String listName){       
        for(UserList list: userLists){        
           if(list.getName().equals(listName)){
           userLists.remove(list);
           break;
          }
        }
       
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
