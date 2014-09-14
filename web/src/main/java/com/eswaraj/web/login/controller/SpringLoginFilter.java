package com.eswaraj.web.login.controller;

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

@Component
public class SpringLoginFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SessionUtil sessionUtil;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        if (sessionUtil.getLoggedInUserFromSession(httpServletRequest) == null) {
            // No user logegd In
            String redirectUrl = "/web/login/facebook?redirect_url=" + httpServletRequest.getRequestURI();
            logger.info("User Not logged In Redirecting to {}", redirectUrl);
            ((HttpServletResponse) response).sendRedirect("/web/login/facebook?redirect_url=" + redirectUrl);
        }

    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}
