package gov.nih.nci.caintegrator.application.configuration;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

public class ConfigurationListener extends ContextLoaderListener {
    public void contextInitialized(final ServletContextEvent servletContextEvent) {
      super.contextInitialized(servletContextEvent);
      SpringContext.initialize(servletContextEvent.getServletContext());
    }
  }
