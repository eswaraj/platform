package com.next.eswaraj.web.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.eswaraj.core.service.PersonService;
import com.eswaraj.web.dto.RegisterFacebookAccountWebRequest;
import com.eswaraj.web.dto.UserDto;

@RestController
public class SpringFacebookLoginController extends BaseSocialLoginController<Facebook> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	//private static final String appPermissions = "email,user_birthday,user_hometown,user_location,user_photos,offline_access";
	private static final String appPermissions = "email,user_birthday,offline_access";

    @Value("${eswaraj_facebook_app_id}")
	private String facebokAppId;
    @Value("${leader_domain_and_context}/web/login/facebooksuccess")
	private String facebookRedirectUrl;
    @Value("${eswaraj_facebook_app_id}")
    private String facebookAppId;

    @Autowired
    private PersonService personService;

    @RequestMapping(value = "/web/login/facebook", method = RequestMethod.GET)
	public ModelAndView login(ModelAndView mv,
			HttpServletRequest httpServletRequest) {
		
        logger.info("Processing Facebook login " + facebookRedirectUrl);
		FacebookConnectionFactory facebookConnectionFactory = (FacebookConnectionFactory)connectionFactoryLocator.getConnectionFactory(Facebook.class);

		OAuth2Operations oauthOperations = facebookConnectionFactory.getOAuthOperations();
		OAuth2Parameters params = new OAuth2Parameters();
		params.setRedirectUri(facebookRedirectUrl);
		params.setScope(appPermissions);
		String authorizeUrl = oauthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, params);
		
        sessionUtil.setRedirectUrlInSessiom(httpServletRequest);

		RedirectView rv = new RedirectView(authorizeUrl);
		logger.info("url= {}", authorizeUrl);
		mv.setView(rv);
		return mv;
	}

    @RequestMapping(value = "/web/login/facebookfail", method = RequestMethod.GET)
	@ResponseBody
	public String loginFailed(HttpServletRequest httpServletRequest, ModelAndView mv) {
		return "Please login to facebook and give permission";
	}

    @RequestMapping(value = "/web/login/facebooksuccess", method = RequestMethod.GET)
	public ModelAndView loginSuccess(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse, ModelAndView mv) {
		try {
			FacebookConnectionFactory facebookConnectionFactory = (FacebookConnectionFactory)connectionFactoryLocator.getConnectionFactory(Facebook.class);
			OAuth2Operations oauthOperations = facebookConnectionFactory.getOAuthOperations();
			String authorizationCode = httpServletRequest.getParameter("code");
            logger.info("authorizationCode= {}", authorizationCode);
            logger.info("facebookRedirectUrl= {}", facebookRedirectUrl);
			AccessGrant accessGrant = oauthOperations.exchangeForAccess(authorizationCode, facebookRedirectUrl, null);
			Connection<Facebook> facebookConnection = facebookConnectionFactory.createConnection(accessGrant);
			
            afterSuccesfullLogin(httpServletRequest, httpServletResponse, facebookConnection);
			
            RegisterFacebookAccountWebRequest registerFacebookAccountWebRequest = new RegisterFacebookAccountWebRequest();
            ConnectionData facebookConnectionData = facebookConnection.createData();
            registerFacebookAccountWebRequest.setExpireTime(facebookConnectionData.getExpireTime());
            registerFacebookAccountWebRequest.setFacebookAppId(facebookAppId);
            registerFacebookAccountWebRequest.setToken(facebookConnectionData.getAccessToken());
            UserDto userDto = personService.registerFacebookAccountWebUser(registerFacebookAccountWebRequest);

            // sessionUtil.setLoggedInUserinSession(httpServletRequest, userDto);
            String redirectUrl = sessionUtil.getAndRemoveRedirectUrlFromSession(httpServletRequest);
            logger.info("redirectUrl= {}", redirectUrl);
            RedirectView rv = new RedirectView(redirectUrl);
            logger.info("url= {}", redirectUrl);
            mv.setView(rv);

		} catch (Exception ex) {
			logger.error("unable to complete facebook login", ex);
            String redirectUrl = httpServletRequest.getContextPath() + "/web/login/facebookfail";
			RedirectView rv = new RedirectView(redirectUrl);
			logger.info("url= {}", redirectUrl);
			mv.setView(rv);
		}
		return mv;
	}

}
