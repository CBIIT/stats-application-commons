/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

/**
 * The List Generator can be used to create List of strings retrived
 * from the formFile sent to it. The assumed format of the file to be parsed is
 * .txt for now
 */
package gov.nih.nci.caintegrator.application.lists;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

/**
 * @author rossok
 * 
 */
public class UserListGenerator {

    private static Logger logger = Logger
            .getLogger(UserListGenerator.class);

    public static List<String> generateList(FileItem formFile) throws IOException {

        List<String> tempList = new ArrayList<String>();

        if ((formFile != null)
                && (formFile.getName().endsWith(".txt") || formFile.getName()
                        .endsWith(".TXT"))) {
            

            try {
                InputStream stream = formFile.getInputStream();
                String inputLine = null;
                BufferedReader inFile = new BufferedReader(
                        new InputStreamReader(stream));
                
                    do {
                        inputLine = inFile.readLine();

                        if (inputLine != null) {
                            if (isAscii(inputLine)) { // make sure all data is ASCII                                                        
                                tempList.add(inputLine);
                            }
                        } else {
                            System.out.println("null line");
                            while (tempList.contains("")) {
                                tempList.remove("");
                            }
                            inFile.close();
                            break;
                        }

                    } while (true);
                } catch (EOFException eof) {
                    logger.error("Errors when reading empty lines in file:" + eof.getMessage());
                } catch (IOException ex) {
                    logger.error("Errors when uploading file:" + ex.getMessage());
                } 
            
            
        }

        return tempList;

    }
    
    public static List<String> generateList(File file){
        List<String> tempList = new ArrayList<String>();
        if (file != null && (file.getName().endsWith(".txt") || file.getName().endsWith(".TXT"))){
            try {
                BufferedReader in = new BufferedReader(new FileReader(file));
                String inputLine = null;
                do {
                    inputLine = in.readLine();

                    if (inputLine != null) {
                        if (isAscii(inputLine)) { // make sure all data is ASCII                                                        
                            tempList.add(inputLine);
                        }
                    } else {
                        System.out.println("null line");
                        while (tempList.contains("")) {
                            tempList.remove("");
                        }
                        in.close();
                        break;
                    }

                } while (true);
            } catch (EOFException eof) {
                logger.error("Errors when reading empty lines in file:" + eof.getMessage());
            } catch (IOException ex) {
                logger.error("Errors when uploading file:" + ex.getMessage());
            } 
        
        
    }

    return tempList;
        
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
