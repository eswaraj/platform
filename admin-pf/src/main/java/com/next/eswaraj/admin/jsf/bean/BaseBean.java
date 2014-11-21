package com.next.eswaraj.admin.jsf.bean;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class BaseBean {

    protected void sendInfoMessage(String summary, String detail) {
        FacesMessage faceMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, faceMessage);
    }

    protected void sendErrorMessage(String summary, String detail) {
        FacesMessage faceMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, faceMessage);
    }

    protected void sendWarningMessage(String summary, String detail) {
        FacesMessage faceMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, faceMessage);
    }
}
