/**
 * This interface will be implemented for any class that will
 * be responsible for uploading user lists into the users session
 */
package gov.nih.nci.caintegrator.application.lists;

import java.io.File;
import java.util.Map;

/**
 * @author rossok
 * 
 */
public interface UploadManager {
    
    public UserList createList(ListType listType, String listName, File formFile);
    
    public UserList validate(UserList unvalidatedList, ListType listType);
    
    public Map getParams(UserList userList);
    
    
}
