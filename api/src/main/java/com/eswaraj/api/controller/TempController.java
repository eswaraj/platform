package com.eswaraj.api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.PersonService;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.messaging.dto.CommentSavedMessage;
import com.eswaraj.messaging.dto.ComplaintViewedByPoliticalAdminMessage;
import com.eswaraj.queue.service.QueueService;
import com.eswaraj.queue.service.aws.impl.AwsUploadUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

@Controller
public class TempController extends BaseController {

    @Autowired
    private PersonService personService;
    @Autowired
    private AppService appService;
    @Autowired
    private QueueService queueService;
    @Autowired
    private AwsUploadUtil awsUploadUtil;

    @RequestMapping(value = "/api/unknown/complaint/viewed", method = RequestMethod.GET)
    public @ResponseBody String sendComplaintViewedMessage(HttpServletRequest httpServletRequest) throws ApplicationException {
        Long complaintId = getLongParameter(httpServletRequest, "complaintId", 0L);
        Long personId = getLongParameter(httpServletRequest, "personId", 0L);
        Long politicalAdminId = getLongParameter(httpServletRequest, "politicalAdminId", 0L);
        ComplaintViewedByPoliticalAdminMessage complaintViewedByPoliticalAdminMessage = new ComplaintViewedByPoliticalAdminMessage();
        complaintViewedByPoliticalAdminMessage.setComplaintId(complaintId);
        complaintViewedByPoliticalAdminMessage.setPersonId(personId);
        complaintViewedByPoliticalAdminMessage.setPoliticalAdminId(politicalAdminId);

        queueService.sendComplaintViewedByPoliticalLeaderMessage(complaintViewedByPoliticalAdminMessage);
        return "Compliant Viewed Message Sent";
    }

    @RequestMapping(value = "/api/unknown/complaint/commented", method = RequestMethod.GET)
    public @ResponseBody String sendComplaintCommentedMessage(HttpServletRequest httpServletRequest) throws ApplicationException {
        Long complaintId = getLongParameter(httpServletRequest, "complaintId", 0L);
        Long personId = getLongParameter(httpServletRequest, "personId", 0L);
        Long politicalAdminId = getLongParameter(httpServletRequest, "politicalAdminId", 0L);
        Long commentId = getLongParameter(httpServletRequest, "commentId", 0L);
        CommentSavedMessage commentSavedMessage = new CommentSavedMessage();
        commentSavedMessage.setComplaintId(complaintId);
        commentSavedMessage.setPersonId(personId);
        commentSavedMessage.setPoliticalAdminId(politicalAdminId);
        commentSavedMessage.setCommentId(commentId);

        queueService.sendCommentSavedMessage(commentSavedMessage);
        return "Comment Saved Message Sent";
    }

    @RequestMapping(value = "/api/unknown/leader/persons", method = RequestMethod.GET)
    public @ResponseBody List<Person> getPersonRecordForLeaders(HttpServletRequest httpServletRequest) throws ApplicationException {

        return appService.getAllPersonsForLeaders();
    }

    @RequestMapping(value = "/api/unknown/leader/persons", method = RequestMethod.POST)
    public @ResponseBody List<Person> savePersonRecordForLeaders(HttpServletRequest httpServletRequest, @RequestBody List<Person> persons) throws ApplicationException {
        List<Person> returnedList = new ArrayList<Person>();
        for (Person onePerson : persons) {
            onePerson.setId(null);
            onePerson.setExternalId(null);
            onePerson.setAddress(null);
            onePerson.setDateCreated(new Date());
            System.out.println("onePerson : " + onePerson);
            onePerson = personService.savePerson(onePerson);
            returnedList.add(onePerson);
        }
        return returnedList;
    }

    @RequestMapping(value = "/api/unknown/leader/mp", method = RequestMethod.POST)
    public @ResponseBody String saveMpPersonRecordForLeaders(HttpServletRequest httpServletRequest, @RequestBody String persons) throws ApplicationException {
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = jsonParser.parse(persons).getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            System.out.println(jsonArray.get(i).getAsJsonObject().get("name").getAsString());
        }
        return persons;
    }

}
