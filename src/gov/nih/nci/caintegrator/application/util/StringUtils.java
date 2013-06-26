/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.util;

import java.util.ArrayList;
import java.util.Iterator;
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
	
	public static String concatinateStrings(List<String> strList, String delim) {
	  StringBuffer sb = new StringBuffer();
	  for (Iterator i=strList.iterator(); i.hasNext(); ) {
	     sb.append(i.next());
	     if (i.hasNext()) sb.append(delim);
	  }
	  return sb.toString();
	}
	
	public static boolean isEmptyStr(String str) {
	  return ((str==null)||(str.trim().length()==0));
	}
	
	/**
	 * This method will get the double represented by the specified string
	 * If the input string is empty or null then null will be returned.
	 * @param doubleStr
	 * @return
	 */
	public static Double getDouble(String doubleStr) {
	  if (!StringUtils.isEmptyStr(doubleStr)) {
	    return Double.valueOf(doubleStr);
	  }
	  return null;
	}

}
