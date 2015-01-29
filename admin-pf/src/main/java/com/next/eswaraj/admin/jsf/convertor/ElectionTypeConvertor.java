package com.next.eswaraj.admin.jsf.convertor;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.ElectionType;
import com.next.eswaraj.admin.jsf.bean.ElectionTypeBean;

@Component
public class ElectionTypeConvertor implements Converter {

    @Autowired
    private ElectionTypeBean electionTypeBean;

    public ElectionTypeConvertor() {
    }


    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                System.out.println("politicalAdminTypeBean : " + electionTypeBean);
                List<ElectionType> electionTypes = electionTypeBean.getElectionTypes();
                long id = Long.parseLong(value);
                for (ElectionType oneElectionType : electionTypes) {
                    if (oneElectionType.getId().equals(id)) {
                        return oneElectionType;
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
            return String.valueOf(((ElectionType) object).getId());
        } else {
            return null;
        }
    }
}
