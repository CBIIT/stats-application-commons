/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.cache;

import gov.nih.nci.caintegrator.application.util.ApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
/**
 * This listener has one purpose and that is to notify the
 * SessionTracker class when the application context has been 
 * loaded or unloaded. Check the application Web.xml for the
 * intialization of this listener.
 * 
 * @author BauerD
 * Feb 17, 2005
 * 
 */




public class ApplicationContextListener implements ServletContextListener {
	private static String contextPath;
	/** 
	 * this method is fired whenever the application server loads the context
	 * that this listener is added to in the web.xml
	 */
	public void contextInitialized(ServletContextEvent contextEvent) {
		ServletContext context = contextEvent.getServletContext();
		contextPath = context.getRealPath("/");
		SessionTracker.setAppplicationRunning(true);
		ApplicationContext.init();
		
	}

	/**
	 * this method is fired whenever the application server unloads 
	 * the context that this listener is added to in the web.xml
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		SessionTracker.setAppplicationRunning(false);

	}
	
	public static String getContextPath() {
		return contextPath;
	}

}
