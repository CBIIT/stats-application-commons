/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.cache;
import java.util.HashMap;

import javax.servlet.http.HttpSession;


/**
 * A simple decorator to avoid casting to HttpSession
 * 
 * @author BauerD
 * Feb 9, 2005
 * 
 */




public class SessionMap extends HashMap{
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	public SessionMap() {
    	super();
    }
  
    public void putSession(HttpSession session) {
    	super.put(session.getId(), session);
    }
    
    public HttpSession getSession(String sessionId) {
    	return (HttpSession)super.get(sessionId);
    }
}
