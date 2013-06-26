/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

/**
 * The userlist bean contains all lists created by the user and has session
 * scope. It is accessed by the UserListBean Helper
 */
package gov.nih.nci.caintegrator.application.lists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rossok
 *
 */
public class UserListBean implements Serializable {

    List<UserList> userLists = new ArrayList<UserList>();
    List<UserList> removedLists = new ArrayList<UserList>();
    public void addList(UserList userList){
    	userList.setName(this.checkListName(userList.getName()));
    	/*
        for(UserList list: userLists){        
            if(list.getName().equals(userList.getName())){
            userLists.remove(list);
            break;
           }
         }
        */        
        userLists.add(userList);
    }
    
    public String checkListName(String listName){
    	String iKey = "_renamed";
    	
    	String cleanName = listName;
    	int i = 0;
    	 for(UserList list: userLists){
    		 
    		 if(list.getName().equalsIgnoreCase(cleanName)){
    			 //hit
    			 if(cleanName.indexOf(iKey)!= -1)	{
    				 String c = cleanName.substring(cleanName.indexOf(iKey)+iKey.length());
    				 Integer it;
    				 try	{
    					 it = Integer.parseInt(c)+1;
    				 }
    				 catch (Exception e) {
    					 it = 1;
					}
    				 cleanName = cleanName.substring(0, cleanName.indexOf(iKey)) +  iKey + it; 
    			 }
    			 else	{
    				 //this is the first hit
    				 cleanName += iKey+"1";
    			 }
    			 cleanName = this.checkListName(cleanName);
    		 }
    	 }
    	//get the names of all the lists in the session
    	//see if listname == any of the current names
    	//if collide, rename new list to listname_1, and recheck until we dont collide
    	return cleanName;
    }
    
    public boolean listExists(String listName){       
        for(UserList list: userLists){        
           if(list.getName().equals(listName)){
        	   return true;
           }
        }
        
        return false;
    }    
    
    public void removeList(String listName){       
        for(UserList list: userLists){        
           if(list.getName().equals(listName)){
           userLists.remove(list);
           removedLists.add(list);
           break;
          }
        }
       
    }
    
    public List<UserList> getEntireList(){
        return userLists;
    }
    
    public List<UserList> getRemovedLists(){
        return removedLists;
    }
    public void clearRemovedLists(){
        removedLists.clear();
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
