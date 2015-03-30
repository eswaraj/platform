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
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
public class PersonBean extends BaseBean {

    @Autowired
    private AdminService adminService;

    @Autowired
    private QueueService queueService;

    private boolean showSearchPanel;

    private Person selectedPerson;

    private List<Person> personSearchResults;

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
            logger.info("personSearchResults {}", personSearchResults);
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

    public void cancel() {
        showSearchPanel = false;
    }

    
}
