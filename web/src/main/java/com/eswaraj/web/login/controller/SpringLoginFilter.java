package com.eswaraj.web.login.controller;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class SpringLoginFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SessionUtil sessionutil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (sessionutil.getLoggedInUserFromSession(request) == null) {
            // No user logegd In
            String redirectUrl = "/web/login/facebook?redirect_url=" + request.getRequestURI();
            logger.info("User Not logged In Redirecting to {}", redirectUrl);
            response.sendRedirect("/web/login/facebook?redirect_url=" + redirectUrl);
        }

    }

}
