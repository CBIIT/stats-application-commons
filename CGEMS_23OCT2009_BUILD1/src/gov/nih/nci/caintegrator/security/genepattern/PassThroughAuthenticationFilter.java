package gov.nih.nci.caintegrator.security.genepattern;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.genepattern.server.database.HibernateUtil;
import org.genepattern.server.user.User;
import org.genepattern.server.user.UserDAO;
import org.genepattern.server.webapp.AuthenticationFilter;
import org.genepattern.server.webapp.jsf.UIBeanHelper;

public class PassThroughAuthenticationFilter extends AuthenticationFilter
{
  private static Logger log = Logger.getLogger(PassThroughAuthenticationFilter.class);

  protected boolean isAuthenticated(HttpServletRequest request, HttpServletResponse response)
  {
    boolean success = super.isAuthenticated(request, response);

    String ticket = request.getParameter("ticket");
    System.out.println("PassThroughAuthFilter: Obtained ticket=" + ticket);
    if ((!(success)) && (ticket != null)) {
      String decodedTicket = EncryptionUtil.decrypt(ticket);
      System.out.println("PassThroughAuthFilter: decrypt ticket=" + decodedTicket);
      String[] splits = decodedTicket.split(":");
      if (!("GP30".equals(splits[1])))
        return success;

      String userId = splits[0];
      System.out.println("PassThroughAuthFilter: userId=" + userId);
      HibernateUtil.beginTransaction();
      User user = new UserDAO().findById(userId);
      if (user == null) {
        System.out.println("Creating new user " + userId);

        User newUser = new User();
        newUser.setUserId(userId);
        try {
          newUser.setPassword(org.genepattern.server.EncryptionUtil.encrypt("t0ps1cr2t"));
        } catch (NoSuchAlgorithmException e) {
          log.error(e);
        }
        new UserDAO().save(newUser);
      }
      try {
        UIBeanHelper.login(userId, false, false, request, response);
        success = true;
      } catch (UnsupportedEncodingException e) {
        log.error(e);
      } catch (IOException e) {
        log.error(e);
      }
    }
    return success;
  }
}