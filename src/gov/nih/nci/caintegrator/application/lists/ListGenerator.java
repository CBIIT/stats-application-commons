/**
 * The List Generator can be used to create List of the specific
 * type sent to it. The assumed format of the file to be parsed is
 * .txt for now
 */
package gov.nih.nci.caintegrator.application.lists;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author rossok
 * 
 */
public class ListGenerator {
    
    private static Logger logger = Logger.getLogger(ListGenerator.class);

    public UserList createList(ListType listType, File formFile, String name) {
        UserList userList = new UserList();
        //set the name
        userList.setName(name);
        //set the list type
        userList.setListType(listType);
        //create temp arraylist to use
        List<String> tempList = new ArrayList<String>();
        
        if ((formFile != null)
                && (formFile.getName().endsWith(".txt") || formFile.getName()
                        .endsWith(".TXT"))) {
            try {
                FileInputStream stream = new FileInputStream(formFile);
                String inputLine = null;
                BufferedReader inFile = new BufferedReader(
                        new InputStreamReader(stream));

                int count = 0;

                while ((inputLine = inFile.readLine()) != null) {
                    if (isAscii(inputLine)) { // make sure all data is ASCII
                        
                        inputLine = inputLine.trim();
                        count++;
                        tempList.add(inputLine);

                    }
                }// end of while

                inFile.close();
            } catch (IOException ex) {
                logger.error("Errors when uploading file:"
                        + ex.getMessage());
            }
        }
        if(tempList.size()>0){
            userList.setList(tempList);
        }
        
        return userList;
    }

    /**
     * <p>
     * Checks whether the string is ASCII 7 bit.
     * </p>
     * 
     * @param str
     *            the string to check
     * @return false if the string contains a char that is greater than 128
     */
    public static boolean isAscii(String str) {
        boolean flag = false;
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) > 128) {
                    return false;
                }
            }
            flag = true;
        }
        return flag;
    }
}
