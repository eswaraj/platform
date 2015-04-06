package com.next.eswaraj.admin.jsf.bean;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseBean {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected void sendInfoMessage(String summary, String detail) {
        FacesMessage faceMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, faceMessage);
    }

    protected void sendErrorMessage(String summary, String detail) {
        sendErrorMessage(null, summary, detail);
    }

    protected void sendErrorMessage(String summary, String detail, Exception ex) {
        sendErrorMessage(null, summary, detail, ex);
    }

    protected void sendErrorMessage(String clientId, String summary, String detail) {
        sendErrorMessage(clientId, summary, detail, null);
    }

    protected void sendErrorMessage(String clientId, String summary, String detail, Exception ex) {
        FacesMessage faceMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
        FacesContext.getCurrentInstance().addMessage(clientId, faceMessage);
        if (ex == null) {
            logger.error(detail);
        } else {
            logger.error(detail, ex);
        }
    }

    protected void sendWarningMessage(String summary, String detail) {
        FacesMessage faceMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, faceMessage);
    }

    protected boolean isValidInput() {
        if (FacesContext.getCurrentInstance().getMessageList().size() > 0) {
            return false;
        }
        return true;
    }

    public void buildAndRedirect(String url) {
        try {
            HttpServletRequest httpServletRequest = getHttpServletRequest();
            FacesContext.getCurrentInstance().getExternalContext().redirect(buildUrl(httpServletRequest, url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshPage() {
        try {
            HttpServletRequest httpServletRequest = getHttpServletRequest();
            String url = httpServletRequest.getRequestURI();
            FacesContext.getCurrentInstance().getExternalContext().redirect(buildUrl(httpServletRequest, url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String buildUrl(HttpServletRequest httpServletRequest, String url) {
        return httpServletRequest.getContextPath() + url;
    }

    public static HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    public void redirect(String url) {
        try {
            System.out.println("Redirecting to " + url);
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
