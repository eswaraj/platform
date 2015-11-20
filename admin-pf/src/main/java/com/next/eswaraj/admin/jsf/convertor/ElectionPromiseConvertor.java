package com.next.eswaraj.admin.jsf.convertor;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.ElectionManifestoPromise;

@Component("jsfElectionPromiseConvertor")
public class ElectionPromiseConvertor implements Converter {

    private List<ElectionManifestoPromise> promises;


    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (promises == null) {
            return null;
        }
        if (value != null && value.trim().length() > 0) {
            try {
                long id = Long.parseLong(value);
                for (ElectionManifestoPromise oneElection : promises) {
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
            return String.valueOf(((ElectionManifestoPromise) object).getId());
        } else {
            return null;
        }
    }

    public List<ElectionManifestoPromise> getPromises() {
        return promises;
    }

    public void setPromises(List<ElectionManifestoPromise> promises) {
        this.promises = promises;
    }
}
