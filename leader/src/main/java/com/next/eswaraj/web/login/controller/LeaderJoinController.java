package com.next.eswaraj.web.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.next.eswaraj.admin.service.AdminService;

@Controller
public class LeaderJoinController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdminService adminService;

    @Autowired
    protected ConnectionFactoryLocator connectionFactoryLocator;

    private static final String appPermissions = "email,user_birthday,offline_access";

    @Value("${eswaraj_facebook_app_id}")
    private String facebokAppId;
    @Value("${leader_domain_and_context}/web/login/facebooksuccess")
    private String facebookRedirectUrl;
    @Value("${eswaraj_facebook_app_id}")
    private String facebookAppId;
    @Value("${leader_domain_and_context}")
    private String leaderServerBaseUrl;

    @RequestMapping(value = "/web/join/eswaraj", method = RequestMethod.GET)
    public ModelAndView join(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) throws ApplicationException {
        String personId = httpServletRequest.getParameter("pid");
        String requestId = httpServletRequest.getParameter("rid");
        String emailId = httpServletRequest.getParameter("eid");
        logger.info("personId : " + personId);
        logger.info("requestId : " + requestId);
        logger.info("emailId : " + emailId);
        adminService.validateJoinRequest(personId, requestId, emailId);
        // build facebook Login URL

        facebookRedirectUrl = leaderServerBaseUrl + "/web/join/eswaraj/fbloginsuccess";
        logger.info("Processing Facebook login " + facebookRedirectUrl);
        FacebookConnectionFactory facebookConnectionFactory = (FacebookConnectionFactory) connectionFactoryLocator.getConnectionFactory(Facebook.class);

        OAuth2Operations oauthOperations = facebookConnectionFactory.getOAuthOperations();
        OAuth2Parameters params = new OAuth2Parameters();
        params.setRedirectUri(facebookRedirectUrl);
        params.setScope(appPermissions);
        String authorizeUrl = oauthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, params);

        // Add parameters to session
        HttpSession httpSession = httpServletRequest.getSession(true);
        httpSession.setAttribute("pid", personId);
        httpSession.setAttribute("rid", requestId);
        httpSession.setAttribute("eid", emailId);

        RedirectView rv = new RedirectView(authorizeUrl);
        logger.info("url= {}", authorizeUrl);
        mv.setView(rv);

        return mv;
    }

    @RequestMapping(value = "/web/join/eswaraj/fbloginsuccess", method = RequestMethod.GET)
    public ModelAndView loginSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ModelAndView mv) throws ApplicationException {
        FacebookConnectionFactory facebookConnectionFactory = (FacebookConnectionFactory) connectionFactoryLocator.getConnectionFactory(Facebook.class);
        OAuth2Operations oauthOperations = facebookConnectionFactory.getOAuthOperations();
        String authorizationCode = httpServletRequest.getParameter("code");
        logger.info("authorizationCode= {}", authorizationCode);
        logger.info("facebookRedirectUrl= {}", facebookRedirectUrl);
        AccessGrant accessGrant = oauthOperations.exchangeForAccess(authorizationCode, facebookRedirectUrl, null);
        Connection<Facebook> facebookConnection = facebookConnectionFactory.createConnection(accessGrant);

        HttpSession httpSession = httpServletRequest.getSession(true);
        String personId = (String) httpSession.getAttribute("pid");
        String requestId = (String) httpSession.getAttribute("rid");
        String emailId = (String) httpSession.getAttribute("eid");

        ConnectionData facebookConnectionData = facebookConnection.createData();
        adminService.linkLeaderToFacebookAccount(personId, requestId, emailId, facebookConnectionData);

        String redirectUrl = leaderServerBaseUrl + "/admin/complaints.xhtml";
        logger.info("redirectUrl= {}", redirectUrl);
        RedirectView rv = new RedirectView(redirectUrl);
        logger.info("url= {}", redirectUrl);
        mv.setView(rv);

        return mv;
    }

}
