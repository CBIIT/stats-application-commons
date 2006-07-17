/**
 * This interface defines the methods to be implemented by the application
 * specific validator class that will check newly created lists and return
 * valid and unvalid lists to the upload manager
 */
package gov.nih.nci.caintegrator.application.lists;

import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

/**
 * @author rossok
 *
 */
public abstract class ListValidator {
    protected List<String> validList = new ArrayList<String>();
    protected List<String> invalidList = new ArrayList<String>();
    
    public ListValidator() {}    
    

    public ListValidator(ListType listType, List<String> unvalidatedList) throws OperationNotSupportedException{
        validate( listType,  unvalidatedList);        
    }
    
    public ListValidator(ListType listType, ListSubType listSubType, List<String> unvalidatedList) throws OperationNotSupportedException{
        validate( listType,  listSubType, unvalidatedList);        
    }
    /**
     * @return Returns the invalidList.
     */
    public List<String> getInvalidList() {
          return invalidList;
    }

    /**
     * @return Returns the validList.
     */
    public List<String> getValidList() {
        return validList;
    };
    

    public abstract void validate (ListType listType, List<String> unvalidatedList) throws OperationNotSupportedException;
 
    public  abstract void validate (ListType listType, ListSubType listSubType, List<String> unvalidatedList) throws OperationNotSupportedException;
    
}
