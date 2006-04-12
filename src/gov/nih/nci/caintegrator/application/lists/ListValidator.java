/**
 * This interface defines the methods to be implemented by the application
 * specific validator class that will check newly created lists and return
 * valid and unvalid lists to the upload manager
 */
package gov.nih.nci.caintegrator.application.lists;

/**
 * @author rossok
 *
 */
public interface ListValidator {
    public UserList getValidList(ListType listType, UserList unvalidatedList);
    
    public UserList getInvalidList();
}
