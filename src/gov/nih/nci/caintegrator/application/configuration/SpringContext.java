package gov.nih.nci.caintegrator.application.configuration;

import java.io.Serializable;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

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
      
    public static Object getBean(final String name) throws IllegalStateException {
      if (!initialized) throw new IllegalStateException("SpringContext has not been initialized.");
      return wac.getBean(name);
    }
  }
