package com.eswaraj.api.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("timingFilter")
public class TimingFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
        logger.info("*****Creating springLoginFilter");

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Long startTime = System.currentTimeMillis();
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("startTime", String.valueOf(startTime));
        chain.doFilter(request, response);

        long timeTakenInMs = System.currentTimeMillis() - startTime;
        logger.info("Total Time taken to proces request {} ms", timeTakenInMs);
        httpServletResponse.setHeader("timeTakenInMs", String.valueOf(timeTakenInMs));
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

}
