package com.next.eswaraj.admin.jsf.bean;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Person;
import com.next.eswaraj.admin.service.AdminService;
import com.next.eswaraj.web.session.SessionUtil;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class PersonSearchBean extends BaseBean {

    @Autowired
    private AdminService adminService;

    @Autowired
    private SessionUtil sessionUtil;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private Person selectedPerson;

    private List<Person> personSearchResults;

    @PostConstruct
    public void init() {

    }

    public List<Person> searchPerson(String query) {
        try {
            if (query.contains(" ")) {
                // Somehow JSF calling this method after selecting an item, so to avoid that doing this check
                // will look for solution later
                logger.info("Not running Search Query");
                return personSearchResults;
            }
            personSearchResults = adminService.searchPersonByName(query);
            return personSearchResults;
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        return null;

    }

    public Person getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public List<Person> getPersonSearchResults() {
        return personSearchResults;
    }

    public void setPersonSearchResults(List<Person> personSearchResults) {
        this.personSearchResults = personSearchResults;
    }

}
