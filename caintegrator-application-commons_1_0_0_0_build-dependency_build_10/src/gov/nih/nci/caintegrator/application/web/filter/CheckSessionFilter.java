package gov.nih.nci.caintegrator.application.web.filter;

import gov.nih.nci.caintegrator.application.util.ApplicationConstants;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/** 
 */
public class CheckSessionFilter implements Filter {

    private FilterConfig filterConfig = null;    

    /**
     * Called by the web container to indicate to a filter that it is being
     * placed into service.
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * The doFilter method of the Filter is called by the container each time a
     * request/response pair is passed through the chain due to a client request
     * for a resource at the end of the chain.
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        boolean authorized = false;
        if (request instanceof HttpServletRequest) {            
            HttpSession session = ((HttpServletRequest) request).getSession(false);
            if(session != null){                
                    authorized = true;                
            }
        }
        if (authorized) {
            chain.doFilter(request, response);
            return;
        } else if (filterConfig != null) {
            String unauthorizedPage = filterConfig.getInitParameter("unauthorizedPage");
            if (unauthorizedPage != null && !"".equals(unauthorizedPage)) {
                filterConfig.getServletContext().getRequestDispatcher(unauthorizedPage).forward(request, response);
                return;
            }
        }
        throw new ServletException("Unauthorized access");

    }

    /**
     * Called by the web container to indicate to a filter that it is being
     * taken out of service.
     */
    public void destroy() {
        filterConfig = null;
    }

}
