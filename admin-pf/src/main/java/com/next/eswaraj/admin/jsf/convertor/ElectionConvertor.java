package com.next.eswaraj.admin.jsf.convertor;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.Election;
import com.next.eswaraj.admin.jsf.bean.ElectionBean;

@Component("jsfElectionConvertor")
public class ElectionConvertor implements Converter {

    @Autowired
    private ElectionBean electionBean;

    public ElectionConvertor() {
    }


    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                System.out.println("politicalAdminTypeBean : " + electionBean);
                List<Election> elections = electionBean.getElections();
                long id = Long.parseLong(value);
                for (Election oneElection : elections) {
                    if (oneElection.getId().equals(id)) {
                        return oneElection;
                    }
                }
                return null;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Election."));
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
            return String.valueOf(((Election) object).getId());
        } else {
            return null;
        }
    }
}
