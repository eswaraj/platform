package com.next.eswaraj.admin.jsf.convertor;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseConvertor<T> implements Converter {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public BaseConvertor() {
    }

    protected List<T> list = new ArrayList<T>();

    public void clear() {
        list.clear();
    }
    @Override
    public final String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            // add all object in list which has been seen through this function
            // and will be used in getAsObject function
            list.add((T) object);
            return getAsString(object);
        } else {
            return null;
        }
    }

    protected abstract String getAsString(Object object);

}
