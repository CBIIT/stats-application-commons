package gov.nih.nci.caintegrator.security.genepattern;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.genepattern.server.UserAccountManager;
import org.genepattern.server.auth.AuthenticationException;
import org.genepattern.server.auth.IAuthenticationPlugin;
import org.genepattern.server.webapp.LoginManager;
import org.genepattern.util.GPConstants;

/**
 * Custom caIntegrator Authentication plugin.
 * 
 * @author sahnih
 */
public class CustomCaIntegratorAuthentication implements IAuthenticationPlugin {
	private static final String T0PS1CR2T = "t0ps1cr2t";
	public CustomCaIntegratorAuthentication() {
    }
    
    /**
     * Redirect to the gp login page.
     */
    public void requestAuthentication(HttpServletRequest request, HttpServletResponse response) 
    throws IOException
    {
        response.sendRedirect(request.getContextPath() + "/pages/login.jsf");        
    }

    /**
     * Parse the http request for username and password pair.
     */
    public String authenticate(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    	 String gp_username = request.getParameter("username");
         String passwordString = request.getParameter("password");
         ////////
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
       
	        if (request.getSession() == null) {
	        	System.out.println("session is null");
	        }else{
	        	//try{
	        	//	gp_username.toString();
	        	//}catch(NullPointerException e){
	        	//	e.printStackTrace();
	        	//}
	        	System.out.println("session is not null");
	        	Enumeration sessionAttributeNames = request.getSession().getAttributeNames();
	            while (sessionAttributeNames.hasMoreElements()) {
	                String key = (String) sessionAttributeNames.nextElement();
	                Object value = request.getAttribute(key);
	                System.out.println("  " + key + " = " + value);
	            }
	        }
         
         ////////
	    if(LoginManager.instance().getUserIdFromSession(request)!= null){ 
	    	gp_username = LoginManager.instance().getUserIdFromSession(request);
	    	   System.out.println("CustomCaIntegratorAuthentication: Already logged in =" + gp_username);
	        	 return gp_username;
	    }
         if(gp_username != null){
	         if (passwordString == null) {
	             passwordString = request.getParameter("loginForm:password");
	         }
	         byte[] password = null;
	         if (passwordString != null) {
	             password = passwordString.getBytes();
	         }
	         boolean authenticated = authenticate(gp_username, password);
	         if (authenticated) {
	             return gp_username;
	         }
         }else{
		        
		        String ticket = request.getParameter("ticket");
		        if (ticket == null) {
		        	ticket = (String) request.getAttribute("ticket");
		        }
		        System.out.println("CustomCaIntegratorAuthentication: Obtained ticket=" + ticket);
		        if (ticket != null) {
		          passwordString = T0PS1CR2T;
		          String decodedTicket = EncryptionUtil.decrypt(ticket);
		          System.out.println("CustomCaIntegratorAuthentication: decrypt ticket=" + decodedTicket);
		          String[] splits = decodedTicket.split(":");
		          gp_username = splits[0];
		          //if (!("GP30".equals(splits[1])))
		         //   return gp_username;
		        
		          System.out.println("CustomCaIntegratorAuthentication: userId=" + gp_username);
		          if (!UserAccountManager.instance().userExists(gp_username)) {
		        	  System.out.println("CustomCaIntegratorAuthentication: user "+ gp_username +" does not exsist so creat new one");
		        	  UserAccountManager.instance().createUser(gp_username, passwordString);
		          }
		          boolean authenticated = authenticate(gp_username, passwordString.getBytes());
				  if (authenticated) {
					  System.out.println("CustomCaIntegratorAuthentication: userId= " + gp_username+ " authenticated");
					  request.setAttribute("ticket", ticket);
				        HttpSession session = request.getSession();
				        if (session == null) {
				        	System.out.println("session is null");
				        }else{
				        	System.out.println("session is not null");
				        session.setAttribute(GPConstants.USERID, gp_username);
				        //TODO: replace all references to 'userID' with 'userid'
				        session.setAttribute("userID", gp_username);
				        LoginManager.instance().addUserIdToSession(request, gp_username);
				        Enumeration sessionAttributeNames = request.getSession().getAttributeNames();
			            while (sessionAttributeNames.hasMoreElements()) {
			                String key = (String) sessionAttributeNames.nextElement();
			                Object value = request.getAttribute(key);
			                System.out.println("  " + key + " = " + value);
			            }
				        }
				      return gp_username;
				  }
		        }
         }
        return null;
    }

    /**
     * Authenticate the username:password by lookup into the GP database.
     */
    public boolean authenticate(String username, byte[] password) throws AuthenticationException {
        return UserAccountManager.instance().authenticateUser(username, password);
    }

    public void logout(String userid, HttpServletRequest request, HttpServletResponse response) {
        //ignore: no action required
    }
}
