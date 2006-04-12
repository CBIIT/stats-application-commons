/**
 * The List Generator can be used to create List of the specific
 * type sent to it. The assumed format of the file to be parsed is
 * .txt for now
 */
package gov.nih.nci.caintegrator.application.lists;

import java.io.File;

/**
 * @author rossok
 *
 */
public class ListGenerator {

    public UserList createList(ListType listType, File formFile){
        return new UserList();
    }

}
