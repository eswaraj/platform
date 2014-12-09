package com.next.eswaraj.admin.jsf.convertor;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.Party;
import com.next.eswaraj.admin.jsf.bean.PoliticalPartyBean;

@Component
public class PartyConvertor implements Converter {

    @Autowired
    private PoliticalPartyBean politicalPartyBean;

    public PartyConvertor() {
    }


    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                List<Party> parties = politicalPartyBean.getParties();
                long id = Long.parseLong(value);
                for (Party oneParty : parties) {
                    if (oneParty.getId().equals(id)) {
                        return oneParty;
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
            return String.valueOf(((Party) object).getId());
        } else {
            return null;
        }
    }
}
