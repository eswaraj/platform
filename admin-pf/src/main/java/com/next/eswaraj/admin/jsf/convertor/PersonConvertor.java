package com.next.eswaraj.admin.jsf.convertor;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.Person;
import com.next.eswaraj.admin.jsf.bean.PoliticalAdminBean;

@Component("jsfPersonConvertor")
public class PersonConvertor implements Converter {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PoliticalAdminBean politicalAdminBean;

    private List<Person> persons;

    public PersonConvertor() {
        logger.info("Creating jsf person convertor");
    }


    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                logger.info("value : " + value);
                List<Person> persons = politicalAdminBean.getPersonSearchResults();
                if (persons == null || persons.isEmpty()) {
                    logger.info("Using persons which are set manually");
                    persons = this.persons;
                }
                if (persons == null || persons.isEmpty()) {
                    logger.info("No Seach Result so returning : {}", politicalAdminBean.getSelectedPerson());
                    return politicalAdminBean.getSelectedPerson();
                } else {
                    long id = Long.parseLong(value);
                    for (Person onePerson : persons) {
                        if (onePerson.getId().equals(id)) {
                            logger.info("onePerson returning : {}", onePerson);
                            return onePerson;
                        }
                    }
                }
                logger.info("return null");
                return null;
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        } else {
            logger.info("return null else");
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

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}
