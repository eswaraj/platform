package com.eswaraj.web.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;

import com.eswaraj.web.admin.controller.BaseController;

public abstract class BaseSocialLoginController<T> extends BaseController {

	@Autowired 
	protected ConnectionFactoryLocator connectionFactoryLocator;
	

	protected void afterSuccesfullLogin(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Connection<T> socialConnection) throws Exception{
        // setLoggedInUserInSesion(httpServletRequest, user);
		/*
		LoginAccountDto userLoginAccounts = aapService.getUserLoginAccounts(user.getId());
		setLoggedInAccountsInSesion(httpServletRequest, userLoginAccounts);
		*/
		
	}
}
