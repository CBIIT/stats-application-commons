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
public class CheckLoginFilter implements Filter {

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
            String isloginpage = ((HttpServletRequest) request).getRequestURI();
            if(isloginpage!=null && isloginpage.endsWith("login.do"))   {
                //just continue, so they can login
                chain.doFilter(request, response);
                return;
            }
            HttpSession session = ((HttpServletRequest) request).getSession();
            if(session != null && session.getAttribute(ApplicationConstants.userInfoBean)!=null){
                gov.nih.nci.caintegrator.application.security.UserInfoBean userInfoBean = (gov.nih.nci.caintegrator.application.security.UserInfoBean) session.getAttribute(ApplicationConstants.userInfoBean);
                if(userInfoBean != null && userInfoBean.isLoggedIn())   {
                    authorized = true;
                }
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

        throw new ServletException("Unauthorized access, unable to forward to login page");

    }

    /**
     * Called by the web container to indicate to a filter that it is being
     * taken out of service.
     */
    public void destroy() {
        filterConfig = null;
    }

}
