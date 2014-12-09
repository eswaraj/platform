package com.next.eswaraj.admin.jsf.convertor;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.PoliticalBodyType;
import com.next.eswaraj.admin.jsf.bean.PoliticalAdminTypeBean;

@Component
public class PoliticalBodyTypeConvertor implements Converter {

    @Autowired
    private PoliticalAdminTypeBean politicalAdminTypeBean;

    public PoliticalBodyTypeConvertor() {
    }


    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                System.out.println("politicalAdminTypeBean : " + politicalAdminTypeBean);
                List<PoliticalBodyType> politicalBodyTypes = politicalAdminTypeBean.getPoliticalBodyTypes();
                long id = Long.parseLong(value);
                for (PoliticalBodyType onePoliticalBodyType : politicalBodyTypes) {
                    if (onePoliticalBodyType.getId().equals(id)) {
                        return onePoliticalBodyType;
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
            return String.valueOf(((PoliticalBodyType) object).getId());
        } else {
            return null;
        }
    }
}
