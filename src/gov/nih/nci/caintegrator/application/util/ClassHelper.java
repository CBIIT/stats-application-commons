/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.util;


/**
 *  
 * Allows the user to pass in a class name and instantiate it.
 * 
 * @author RossoK
 *
 */




public class ClassHelper {
	/**
	 * This helper method will return the class if it is found
	 *
	 * 
	 */
	    
    public static Class createClass(String className){
        Class classInstance = null;
        try {
            classInstance = Class.forName(className);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return classInstance;
    }
}
