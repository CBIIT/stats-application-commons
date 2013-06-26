/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

/**
 * 
 */
package gov.nih.nci.caintegrator.application.zip;

import gov.nih.nci.caintegrator.dto.critieria.Constants;
import gov.nih.nci.caintegrator.exceptions.ValidationException;

import org.apache.commons.validator.EmailValidator;


/**
 * @author sahnih
 *
 */
public class FileNameGenerator {
    public static String generateFileName(String userName) throws ValidationException{
    	String fileName = null;
    	if(userName != null ){
	    	// Get a random number of up to 7 digits   	
	    	int sevenDigitRandom = new Double(Math.random() * 10000000).intValue();
	    	
	    	// Get the last 7 digits of the Java timestamp
	    	String s = String.valueOf(System.currentTimeMillis());
	    	String lastSevenOfTimeStamp = s.substring(s.length()-7, s.length());
	    	
	    	// Put it all together
	        fileName = userName + "_" + sevenDigitRandom + lastSevenOfTimeStamp;
    	}
    	else{
    		throw new ValidationException("Invalid Email Address");
    	}
        return fileName;
    }
    public static String generateFileNameFromEmail(String userEmail) throws ValidationException{
    	String fileName = null;
    	if(userEmail != null && EmailValidator.getInstance().isValid(userEmail)){
	    	// Get a random number of up to 7 digits   	
	    	int sevenDigitRandom = new Double(Math.random() * 10000000).intValue();
	    	
	    	// Get the last 7 digits of the Java timestamp
	    	String s = String.valueOf(System.currentTimeMillis());
	    	String lastSevenOfTimeStamp = s.substring(s.length()-7, s.length());
	    	
	    	// Get the string before the @ sign in the user's email address
	    	String userEmailBeforeAtSign = userEmail.substring(0, userEmail.indexOf('@'));
	    	
	    	// Put it all together
	        fileName = userEmailBeforeAtSign + "_" + sevenDigitRandom + lastSevenOfTimeStamp;
    	}
    	else{
    		throw new ValidationException("Invalid Email Address");
    	}
        return fileName;
    }
    public static String generateUniqueFileName(String fileName, String type, String platform){
    	String uniqueZipFileName = null;
    	String platformID = null;

    	if(fileName != null && type != null && platform != null){
    		if (platform.equals(Constants.AFFY_OLIGO_PLATFORM)){
    			platformID = "AFFY-U133P2";
    		}
    		else if (platform.equals(Constants.AFFY_100K_SNP_ARRAY)){
    			platformID = "AFFY-100K-SNP";
    		}
    		uniqueZipFileName = createFilename(fileName);
	    	// Get a random number of up to 7 digits   	
	    	int sevenDigitRandom = new Double(Math.random() * 10000000).intValue();
	    	
	    	// Get the last 7 digits of the Java timestamp
	    	String s = String.valueOf(System.currentTimeMillis());
	    	String lastSevenOfTimeStamp = s.substring(s.length()-7, s.length());
	    	
	
	    	// Put it all together
	    	uniqueZipFileName = fileName +"_"+platformID+"_"+type+ "_" + sevenDigitRandom + lastSevenOfTimeStamp;
    	}
        return uniqueZipFileName;
    }
	/**
	 * Returns a name suitable for using as a filename. Replaces " " with "-".
	 * 
	 * @param experimentName
	 * @return
	 */
	protected static String createFilename(String fileName) {
		StringBuffer buf = new StringBuffer(removeSpaces(fileName));
		return new String(buf);
	}

	protected static String removeSpaces(String name) {
		StringBuffer buf = new StringBuffer(name.length());
		for (Character c : name.toCharArray()) {
			if (' ' == c || '\'' == c || '\"' == c || '?'  == c || ',' ==c || '&' == c ||'“' == c ||'‘' == c || '#' == c )
				buf.append('_');
			else
				buf.append(c);
		}
		return new String(buf);
	}
}
