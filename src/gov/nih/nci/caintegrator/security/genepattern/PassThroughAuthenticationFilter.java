/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.security.genepattern;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.genepattern.server.UserAccountManager;
import org.genepattern.server.auth.AuthenticationException;
import org.genepattern.server.database.HibernateUtil;
import org.genepattern.server.user.User;
import org.genepattern.server.user.UserDAO;
import org.genepattern.server.webapp.AuthenticationFilter;
import org.genepattern.server.webapp.LoginManager;
import org.genepattern.server.webapp.jsf.UIBeanHelper;
import org.genepattern.util.GPConstants;

public class PassThroughAuthenticationFilter extends AuthenticationFilter
{
  private static Logger log = Logger.getLogger(PassThroughAuthenticationFilter.class);
  private static final String T0PS1CR2T = "t0ps1cr2t";
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) 
  throws IOException, ServletException 
  {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
	    boolean success = super.isAuthenticated(request);
	    System.out.println("Request parameterNames:");
	    Enumeration parameterNames = request.getParameterNames();
	    while (parameterNames.hasMoreElements()) {
	        String key = (String) parameterNames.nextElement();
	        Object value = request.getParameter(key);
	        System.out.println("  " + key + " = " + value);
	    }
	    System.out.println("Request attributeNames:");
	    Enumeration attributeNames = request.getAttributeNames();
	    while (attributeNames.hasMoreElements()) {
	        String key = (String) attributeNames.nextElement();
	        Object value = request.getAttribute(key);
	        System.out.println("  " + key + " = " + value);
	    }
		String ticket = request.getParameter("ticket");
	    if (ticket == null) {
	    	ticket = (String) request.getAttribute("ticket");
	    }
	    if ((!(success)) && (ticket != null)) {

	        System.out.println("PassThroughAuthFilter: Obtained ticket=" + ticket);
	          String passwordString = T0PS1CR2T;
	          String decodedTicket = EncryptionUtil.decrypt(ticket);
	          System.out.println("CustomCaIntegratorAuthentication: decrypt ticket=" + decodedTicket);
	          String[] splits = decodedTicket.split(":");
	          String gp_username = splits[0];
	          //if (!("GP30".equals(splits[1])))
	         //   return gp_username;
	        
	          System.out.println("CustomCaIntegratorAuthentication: userId=" + gp_username);
	          boolean authenticated = false;
	          if (!UserAccountManager.instance().userExists(gp_username)) {
	        	  System.out.println("CustomCaIntegratorAuthentication: user "+ gp_username +" does not exsist so creat new one");
	        	  try {
					UserAccountManager.instance().createUser(gp_username, passwordString);
				} catch (AuthenticationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	          }
	          
			try {
				authenticated = UserAccountManager.instance().authenticateUser(gp_username, passwordString.getBytes());
			} catch (AuthenticationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  if (authenticated) {
				  System.out.println("CustomCaIntegratorAuthentication: userId= " + gp_username+ " authenticated");
				  request.setAttribute("ticket", ticket);
			        HttpSession session = request.getSession();
			        if (session == null) {
			        	System.out.println("session is null");
			        }else{
			        	System.out.println("session is NOT null");
			        session.setAttribute(GPConstants.USERID, gp_username);
			        //TODO: replace all references to 'userID' with 'userid'
			        session.setAttribute("userID", gp_username);
			        LoginManager.instance().addUserIdToSession(request, gp_username);
			        }
			      success = true;
			      response.sendRedirect(request.getContextPath() + "/pages/login.jsf"); 
			  }
	    }
	    return;
  }
}