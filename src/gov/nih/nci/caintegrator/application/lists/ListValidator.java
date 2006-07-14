/**
 * This interface defines the methods to be implemented by the application
 * specific validator class that will check newly created lists and return
 * valid and unvalid lists to the upload manager
 */
package gov.nih.nci.caintegrator.application.lists;

import java.util.List;

import javax.naming.OperationNotSupportedException;

/**
 * @author rossok
 *
 */
public interface ListValidator {
    public List getValidList(ListType listType, List<String> unvalidatedList) throws OperationNotSupportedException;
    
    public List getInvalidList(ListType listType, List<String> unvalidatedList) throws OperationNotSupportedException;
    
    public List getValidList(ListType listType, ListSubType listSubType, List<String> unvalidatedList) throws OperationNotSupportedException ;
    
    public List getInvalidList(ListType listType, ListSubType listSubType, List<String> unvalidatedList) throws OperationNotSupportedException ;

    public List getValidList() throws OperationNotSupportedException;
    
    public List getInvalidList() throws OperationNotSupportedException;
    
}
