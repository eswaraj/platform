package com.eswaraj.core.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

@Configuration
public class SocialConfig {
    @Value("${eswaraj_facebook_app_id}")
    private String facebookAppId;
    @Value("${eswaraj_facebook_app_secret}")
    private String facebookAppSecret;
	
    @Bean
    public ConnectionFactoryLocator connectionFactoryLocator() {

        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(new FacebookConnectionFactory(facebookAppId, facebookAppSecret));
        return registry;
    }
    
    /*
    @Bean
    @Scope(value="singleton", proxyMode=ScopedProxyMode.INTERFACES)
    public UsersConnectionRepository usersConnectionRepository() {
        return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator(), textEncryptor());
    }
    
    @Bean
    public ConnectController connectController() {
        ConnectController controller = new ConnectController(
            connectionFactoryLocator(), usersConnectionRepository().createConnectionRepository("ravit"));
        //controller.setApplicationUrl(environment.getProperty("application.url");
        controller.addInterceptor(new ConnectInterceptor<Twitter>() {

			@Override
			public void preConnect(
					ConnectionFactory<Twitter> connectionFactory,
					MultiValueMap<String, String> parameters, WebRequest request) {
			}

			@Override
			public void postConnect(Connection<Twitter> connection,
					WebRequest request) {
				//usersConnectionRepository().createConnectionRepository("ravit").addConnection(connection);
			}

			
		});
        controller.addInterceptor(new ConnectInterceptor<Facebook>() {

			@Override
			public void preConnect(
					ConnectionFactory<Facebook> connectionFactory,
					MultiValueMap<String, String> parameters, WebRequest request) {
				
			}

			@Override
			public void postConnect(Connection<Facebook> connection,
					WebRequest request) {
				
			}

			
		});
        return controller;
    }
    
    
    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.noOpText();
    }
    */

}