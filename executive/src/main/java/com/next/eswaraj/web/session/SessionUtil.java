package com.next.eswaraj.web.session;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.eswaraj.web.dto.UserDto;
import com.google.gdata.util.common.base.StringUtil;

@Component
public class SessionUtil {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    protected static final String REDIRECT_URL_PARAM_ID = "redirect_url";
    protected static final String LOGGED_IN_USER_SESSION_PARAM = "LIUSP";

    public String getRedirectUrlForRedirectionAfterLogin(HttpServletRequest httpServletRequest) {
        String redirectUrlAfterLogin = getRedirectUrl(httpServletRequest);
        logger.info("redirectUrlAfterLogin from param = " + redirectUrlAfterLogin);
        if (StringUtil.isEmpty(redirectUrlAfterLogin)) {
            redirectUrlAfterLogin = httpServletRequest.getContextPath() + "/index.html";
            logger.info("redirectUrlAfterLogin default = " + redirectUrlAfterLogin);
        }
        return redirectUrlAfterLogin;
    }

    public String getRedirectUrl(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter(REDIRECT_URL_PARAM_ID);
    }

    public void setRedirectUrlInSessiom(HttpServletRequest httpServletRequest) {
        String redirectUrl = getRedirectUrlForRedirectionAfterLogin(httpServletRequest);
        httpServletRequest.getSession(true).setAttribute(REDIRECT_URL_PARAM_ID, redirectUrl);
    }

    public String getAndRemoveRedirectUrlFromSession(HttpServletRequest httpServletRequest) {
        String redirectUrl = (String) httpServletRequest.getSession().getAttribute(REDIRECT_URL_PARAM_ID);
        if (StringUtil.isEmpty(redirectUrl)) {
            redirectUrl = httpServletRequest.getContextPath() + "/index.html";
        } else {
            httpServletRequest.getSession().removeAttribute(REDIRECT_URL_PARAM_ID);
        }

        return redirectUrl;
    }

    public void setLoggedInUserinSession(HttpServletRequest httpServletRequest, UserDto user) {
        httpServletRequest.getSession(true).setAttribute(LOGGED_IN_USER_SESSION_PARAM, user);
    }

    public UserDto getLoggedInUserFromSession(HttpServletRequest httpServletRequest) {
        return (UserDto) httpServletRequest.getSession().getAttribute(LOGGED_IN_USER_SESSION_PARAM);
    }

    public UserDto getLoggedInUserFromSession() {
        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return getLoggedInUserFromSession(httpServletRequest);
    }

}
