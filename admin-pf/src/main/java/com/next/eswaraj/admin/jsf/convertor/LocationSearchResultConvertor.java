package com.next.eswaraj.admin.jsf.convertor;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.extended.LocationSearchResult;

@Component
public class LocationSearchResultConvertor extends BaseConvertor<LocationSearchResult> {

    public LocationSearchResultConvertor() {
    }

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                long id = Long.parseLong(value);
                for (LocationSearchResult oneLocation : list) {
                    if (oneLocation.getLocation().getId().equals(id)) {
                        return oneLocation;
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
    protected String getAsString(Object object) {
        if (object != null) {
            return String.valueOf(((LocationSearchResult) object).getLocation().getId());
        } else {
            return null;
        }
    }

}
