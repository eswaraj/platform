package com.next.eswaraj.admin.jsf.convertors;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.Person;
import com.next.eswaraj.admin.jsf.bean.PersonSearchBean;

@Component
public class PersonJsfConvertor implements Converter {

    @Autowired
    private PersonSearchBean personSearchBean;

    public PersonJsfConvertor() {
    }


    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                List<Person> persons = personSearchBean.getPersonSearchResults();
                if (persons == null) {
                    return personSearchBean.getSelectedPerson();
                } else {
                    long id = Long.parseLong(value);
                    for (Person onePerson : persons) {
                        if (onePerson.getId().equals(id)) {
                            return onePerson;
                        }
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
            return String.valueOf(((Person) object).getId());
        } else {
            return null;
        }
    }
}
