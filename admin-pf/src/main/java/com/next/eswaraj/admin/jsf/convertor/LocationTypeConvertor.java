package com.next.eswaraj.admin.jsf.convertor;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.LocationType;
import com.next.eswaraj.admin.jsf.bean.PoliticalAdminTypeBean;

@Component("jsfLocationTypeConvertor")
public class LocationTypeConvertor implements Converter {

    @Autowired
    private PoliticalAdminTypeBean politicalAdminTypeBean;

    public LocationTypeConvertor() {
    }


    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                System.out.println("politicalAdminTypeBean : " + politicalAdminTypeBean);
                List<LocationType> locationTypes = politicalAdminTypeBean.getLocationTypes();
                long id = Long.parseLong(value);
                for(LocationType oneLocationType : locationTypes){
                    if (oneLocationType.getId().equals(id)) {
                        return oneLocationType;
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
            return String.valueOf(((LocationType) object).getId());
        } else {
            return null;
        }
    }
}
