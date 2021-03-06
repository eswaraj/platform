package com.next.eswaraj.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.webapp.FacesServlet;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.JtaTransactionManagerFactoryBean;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jExceptionTranslator;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.mapping.Neo4jMappingContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.DispatcherServlet;

import com.next.eswaraj.admin.jsf.filter.SpringLoginFilter;
import com.next.eswaraj.admin.util.SessionUtil;

@Configuration
@ComponentScan(basePackages = { "com.next.eswaraj.admin.login.controller", "com.next.eswaraj.admin.util", "com.next.eswaraj.admin.jsf.bean", "com.next.eswaraj.admin.service",
        "com.next.eswaraj.admin.jsf.convertor" })
@EnableNeo4jRepositories(basePackages = "com.eswaraj.domain.repo")
@EnableScheduling
@ImportResource({ "classpath:eswaraj-core.xml", "classpath:eswaraj-web-admin-context.xml" })
public class Main extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    
    @Value("${db_url}")
    private String dbUrl;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(new Class[] { Main.class, Initializer.class, XmlResource.class });
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        FacesServlet servlet = new FacesServlet();  
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(servlet, "*.jsf", "*.xhtml");
		return servletRegistrationBean;
    }
    
    @Bean
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        return dispatcherServlet;
    }

    /**
     * Register dispatcherServlet programmatically
     * 
     * @return ServletRegistrationBean
     */
    @Bean
    public ServletRegistrationBean dispatcherServletRegistration() {

        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet(), "/");

        registration.setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);


        return registration;
    }

    @ImportResource({ "classpath:eswaraj-domain.xml", "classpath:eswaraj-web-admin-context.xml" })
    public static class XmlResource {

    }

    @Bean
    public GraphDatabaseService graphDatabaseService() {
        System.out.println("Creating DB Conenction at URL : " + dbUrl);
        return new SpringRestGraphDatabase(dbUrl);
    }

    @Bean(name = "neo4jTemplate")
    public Neo4jTemplate neo4jTemplate() {
        return new Neo4jTemplate(graphDatabaseService());
    }

    @Bean
    public Neo4jMappingContext neo4jMappingContext() {
        return new Neo4jMappingContext();
    }

    @Bean
    public JtaTransactionManagerFactoryBean transactionManager() throws Exception {
        return new JtaTransactionManagerFactoryBean(graphDatabaseService());
    }

    @Bean
    public Neo4jExceptionTranslator exceptionTranslator() {
        return new Neo4jExceptionTranslator();
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        SpringLoginFilter securityFilter = new SpringLoginFilter();
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("*.xhtml");
        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.setFilter(securityFilter);
        return registrationBean;
    }

    // Put Servlets and Filters in their own nested class so they don't force early
    // instantiation of ManagementServerProperties.
    @Configuration
    protected static class ApplicationContextFilterConfiguration {

        @Bean
        public Filter applicationContextIdFilter(ApplicationContext context) {
            return new ApplicationContextHeaderFilter(context);
        }

    }

    /**
     * {@link OncePerRequestFilter} to add the {@literal X-Application-Context} if required.
     */
    private static class ApplicationContextHeaderFilter extends OncePerRequestFilter {

        private final ApplicationContext applicationContext;

        SessionUtil sessionUtil;

        public ApplicationContextHeaderFilter(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            // System.out.println("Doing Internal Filtering " + request.getPathInfo() + "," + request.getRequestURL().toString() + ", sessionUtil=" + sessionUtil);
            filterChain.doFilter(request, response);
        }

    }
    


}
