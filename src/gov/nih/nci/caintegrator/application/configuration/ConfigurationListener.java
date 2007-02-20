package gov.nih.nci.caintegrator.application.configuration;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

/**
 * This servlet extends the spring ContextLoaderListener.  It still
 * allows you to load spring resources on startup, but this will
 * allow a user to access spring beans more easily from
 * any point within the application.
 * 
 *
 * @author caIntegrator Team
 */
public class ConfigurationListener extends ContextLoaderListener {
    public void contextInitialized(final ServletContextEvent servletContextEvent) {
      super.contextInitialized(servletContextEvent);
      SpringContext.initialize(servletContextEvent.getServletContext());
    }
  }
