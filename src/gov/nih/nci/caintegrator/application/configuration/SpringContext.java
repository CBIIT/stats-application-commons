/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.configuration;

import java.io.Serializable;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Helper class that provides an easy way to load beans from any point in the application,
 * not just from a spring managed bean.
 * 
 *
 * @author caIntegrator Team
 */
public class SpringContext implements Serializable {
    
    private static final long serialVersionUID = 3125223827888524892L;
         
    private static WebApplicationContext wac;
         
    private static boolean initialized = false;
      
    private SpringContext() {}
     
    protected static synchronized WebApplicationContext initialize(final ServletContext servletContext) {
      if (wac == null) {
        wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        initialized = true;
      }
      return wac;
    }
    
    /**
     * Returns a bean from the spring ApplicationFactory with the
     * provided name.
     * 
     * @param name
     * @return
     * @throws IllegalStateException
     */
    public static Object getBean(final String name) throws IllegalStateException {
      if (!initialized) throw new IllegalStateException("SpringContext has not been initialized.");
      return wac.getBean(name);
    }
  }
