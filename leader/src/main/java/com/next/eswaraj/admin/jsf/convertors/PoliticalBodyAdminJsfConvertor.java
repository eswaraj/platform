package com.next.eswaraj.admin.jsf.convertors;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.next.eswaraj.admin.jsf.bean.LoginBean;

@Component
public class PoliticalBodyAdminJsfConvertor implements Converter {

    @Autowired
    private LoginBean loginBean;

    public PoliticalBodyAdminJsfConvertor() {
    }


    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                System.out.println("complaintsBean : " + loginBean);
                List<PoliticalBodyAdmin> politicalBodyAdmins = loginBean.getUserPoliticalBodyAdmins();
                long id = Long.parseLong(value);
                for (PoliticalBodyAdmin onePoliticalBodyAdmin : politicalBodyAdmins) {
                    if (onePoliticalBodyAdmin.getId().equals(id)) {
                        return onePoliticalBodyAdmin;
                    }
                }
                return null;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            if (object instanceof String) {
                return "";
            }
            return String.valueOf(((PoliticalBodyAdmin) object).getId());
        } else {
            return null;
        }
    }
}
