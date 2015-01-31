package com.next.eswaraj.admin.jsf.convertor;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.SystemCategory;
import com.next.eswaraj.admin.jsf.bean.SystemCategoryBean;

@Component
public class SystemCategoryConvertor implements Converter {

    @Autowired
    private SystemCategoryBean systemCategoryBean;

    public SystemCategoryConvertor() {
    }


    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                List<SystemCategory> SystemCategories = systemCategoryBean.getSystemCategories();
                long id = Long.parseLong(value);
                for (SystemCategory oneSystemCategory : SystemCategories) {
                    if (oneSystemCategory.getId().equals(id)) {
                        return oneSystemCategory;
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
                return (String) object;
            }
            return String.valueOf(((SystemCategory) object).getId());
        } else {
            return null;
        }
    }
}
