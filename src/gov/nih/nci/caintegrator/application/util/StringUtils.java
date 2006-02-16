package gov.nih.nci.caintegrator.application.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

	/**
	 * This method will extract tokens from a string containing multiple tokens 
	 * and place the tokens in a collection.  This is used for example, to parse strings containing multiple GO or PATHWAY ids.
	 * @param tokenCollection
	 * @param tokenStr
	 * @param delimeter
	 */
	public static List<String> extractTokens(String text, String delim) {
	  List<String> tokenList = new ArrayList<String>();
	  if (text != null) {
	    String[] tokens = text.split(delim);
	  
	    for (int i=0; i < tokens.length; i++) {
	      tokenList.add(tokens[i].trim());
	    }
	  }
	  return tokenList;
	}

}
