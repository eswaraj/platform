package com.next.eswaraj.web.filters;

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

import com.next.eswaraj.web.session.SessionUtil;

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
        logger.info("Requested URL " + httpServletRequest.getRequestURL().toString());
        if (!httpServletRequest.getRequestURI().contains("login.xhtml") && sessionUtil.getLoggedInUserFromSession(httpServletRequest) == null) {
            // No user logegd In
            String redirectUrl = "/admin/login.xhtml?redirect_url=" + httpServletRequest.getRequestURI();
            logger.info("User Not logged In Redirecting to {}", redirectUrl);
            ((HttpServletResponse) response).sendRedirect(redirectUrl);
            return;
        }
        chain.doFilter(httpServletRequest, response);

    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}
