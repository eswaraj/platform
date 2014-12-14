package com.next.eswaraj.admin;

import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Initializer implements ServletContextInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.err.println("------------------------------------");
        servletContext.setInitParameter("primefaces.CLIENT_SIDE_VALIDATION", "true");
        /*
        Dynamic dynamic = servletContext.addFilter("springLoginFilter", "org.springframework.web.filter.DelegatingFilterProxy");
        dynamic.getUrlPatternMappings().add("*.xhtml");
        dynamic.getUrlPatternMappings().add("*.jsf");
        */
    }

}