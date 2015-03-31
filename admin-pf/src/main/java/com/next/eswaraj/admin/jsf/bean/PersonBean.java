package com.next.eswaraj.admin.jsf.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.Person;
import com.eswaraj.queue.service.QueueService;
import com.next.eswaraj.admin.jsf.convertor.PersonConvertor;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
public class PersonBean extends BaseBean {

    private static final long serialVersionUID = 1L;

    @Autowired
    private AdminService adminService;

    @Autowired
    private QueueService queueService;
    
    @Autowired
    private PersonConvertor personConvertor;

    private boolean showSearchPanel;

    private Person selectedPerson;

    private List<Person> personSearchResults;

    private boolean updateMode;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init() {
        showSearchPanel = true;
        try {
            logger.info("Getting Location From DB");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Person> searchPerson(String query) {
        try {
            logger.info("Searching Person for {}", query);
            personSearchResults = adminService.searchPersonByName(query);
            logger.info("personSearchResults {}", personSearchResults.size());
            personConvertor.setPersons(personSearchResults);
            return personSearchResults;
        } catch (Exception e) {
            sendErrorMessage("Error", "Unable to search person", e);
        }
        return null;

    }

    public void savePerson(ActionEvent event) {
        logger.info("saving Person " + selectedPerson);

        try {
            selectedPerson = adminService.savePerson(selectedPerson);
            logger.info("Saved Person " + selectedPerson);
            queueService.sendRefreshPerson(selectedPerson.getId(), "web");
        } catch (Exception e) {
            sendErrorMessage("Error", e.getMessage());
            e.printStackTrace();
            logger.error("Error", e);
        }
    }

    public void editPerson() {
        System.out.println("Edit Person : " + selectedPerson);
        showSearchPanel = false;
    }

    public void cancel() {
        System.out.println("Cancel Person : " + selectedPerson);
        showSearchPanel = true;
    }

    public boolean isShowSearchPanel() {
        return showSearchPanel;
    }

    public void setShowSearchPanel(boolean showSearchPanel) {
        this.showSearchPanel = showSearchPanel;
    }

    public Person getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(Person selectedPerson) {
        System.out.println("Set Person : " + selectedPerson);
        this.selectedPerson = selectedPerson;
    }

    public boolean isUpdateMode() {
        return updateMode;
    }

    public void setUpdateMode(boolean updateMode) {
        this.updateMode = updateMode;
    }
    
}
