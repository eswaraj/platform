package com.next.eswaraj.admin.jsf.convertor;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.extended.LocationSearchResult;
import com.next.eswaraj.admin.jsf.bean.PoliticalAdminBean;

@Component
public class LocationSearchResultConvertor implements Converter {

    @Autowired
    private PoliticalAdminBean politicalAdminBean;

    public LocationSearchResultConvertor() {
    }


    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                List<LocationSearchResult> locations = politicalAdminBean.getLocationSearchResults();
                long id = Long.parseLong(value);
                for (LocationSearchResult oneLocation : locations) {
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
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((LocationSearchResult) object).getLocation().getId());
        } else {
            return null;
        }
    }
}