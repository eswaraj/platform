package com.next.eswaraj.admin.jsf.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.web.dto.UserDto;
import com.next.eswaraj.admin.util.SessionUtil;

@Component("springLoginFilter")
public class SpringLoginFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SessionUtil sessionUtil;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
        logger.info("*****Creating springLoginFilter");
        if (sessionUtil == null) {
            sessionUtil = new SessionUtil();
        }

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        // logger.info("Requested URL " + httpServletRequest.getRequestURL().toString());
        UserDto user = sessionUtil.getLoggedInUserFromSession(httpServletRequest);
        // logger.info("Logged In User = " + user);
        if (user == null) {
            // No user logegd In
            String redirectUrl = "/web/login/facebook?redirect_url=" + httpServletRequest.getRequestURI();
            logger.info("User Not logged In, SO Redirecting to {}", redirectUrl);
            ((HttpServletResponse) response).sendRedirect(redirectUrl);
            return;
        }
        if (user.getPerson().getEmail().equalsIgnoreCase("ping2ravi@yahoo.com") || user.getPerson().getEmail().equalsIgnoreCase("helloshailendra@yahoo.co.in")
                || user.getPerson().getEmail().equalsIgnoreCase("rakesh.pratap@gmail.com") || user.getPerson().getEmail().equalsIgnoreCase("pradeep.sonkar@gmail.com")) {
            chain.doFilter(httpServletRequest, response);
            return;
        }
        String redirectUrl = "/admin/notAllowed.html";
        logger.info("User Not Allowed So Redirecting to {}", redirectUrl);
        ((HttpServletResponse) response).sendRedirect(redirectUrl);
        return;

    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}
