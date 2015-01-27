package com.eswaraj.api.controller;

import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.core.service.PersonService;
import com.eswaraj.core.service.SettingService;
import com.eswaraj.domain.nodes.extended.LocationSearchResult;
import com.eswaraj.domain.nodes.extended.PoliticalBodyAdminSearchResult;
import com.eswaraj.queue.service.QueueService;
import com.google.gson.Gson;

@Controller
public class SearchController extends BaseController {

    @Autowired
    private PersonService personService;
    @Autowired
    private AppService appService;
    @Autowired
    private QueueService queueService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private LocationService locationService;

    Gson gson = new Gson();

    @RequestMapping(value = "/api/search", method = RequestMethod.GET)
    public @ResponseBody String sendComplaintViewedMessage(HttpServletRequest httpServletRequest) throws ApplicationException {
        String searchParam = httpServletRequest.getParameter("q");
        if(StringUtils.isEmpty(searchParam)){
            return "[]";//empty array
        }
        SortedSet<SearchResult> searchResults = new TreeSet<SearchController.SearchResult>(new Comparator<SearchResult>() {

            @Override
            public int compare(SearchResult o1, SearchResult o2) {
                // TODO Auto-generated method stub
                return o1.getName().compareTo(o2.getName());
            }
        });

        addLocationSearchResult(searchResults, searchParam);
        addPoliticalAdminSearchResult(searchResults, searchParam);

        return gson.toJson(searchResults);
    }

    private void addLocationSearchResult(SortedSet<SearchResult> searchResults, String searchParam) {
        try {
            List<LocationSearchResult> locationSearchResult = locationService.searchLocationByName(searchParam);
            for (LocationSearchResult oneLocationSearchResult : locationSearchResult) {
                searchResults.add(new SearchResult(oneLocationSearchResult));
            }
        } catch (Exception ex) {
            logger.error("Unable to search location", ex);
        }
    }

    private void addPoliticalAdminSearchResult(SortedSet<SearchResult> searchResults, String searchParam) {
        try {
            List<PoliticalBodyAdminSearchResult> politicalBodyAdminSearchResult = personService.searchPoliticalBodyAdminByName(searchParam);
            for (PoliticalBodyAdminSearchResult oneLeaderSearchResult : politicalBodyAdminSearchResult) {
                searchResults.add(new SearchResult(oneLeaderSearchResult));
            }
        } catch (Exception ex) {
            logger.error("Unable to search location", ex);
        }
    }

    private static class SearchResult {
        private Long id;
        private String type;
        private String subType;
        private String name;
        private String image;
        private String partyName;
        private String cName;

        public SearchResult(LocationSearchResult locationSearchResult) {
            this.id = locationSearchResult.getLocation().getId();
            this.type = "Location";
            this.subType = locationSearchResult.getLocationType().getName();
            this.name = locationSearchResult.getLocation().getName();
        }

        public SearchResult(PoliticalBodyAdminSearchResult politicalBodyAdminSearchResult) {
            this.id = politicalBodyAdminSearchResult.getPoliticalBodyAdmin().getId();
            this.type = "Leader";
            this.subType = politicalBodyAdminSearchResult.getPoliticalBodyType().getName();
            this.name = politicalBodyAdminSearchResult.getPerson().getName();
            this.image = politicalBodyAdminSearchResult.getPerson().getProfilePhoto();
            this.partyName = politicalBodyAdminSearchResult.getParty().getName();
            this.cName = politicalBodyAdminSearchResult.getLocation().getName();
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSubType() {
            return subType;
        }

        public void setSubType(String subType) {
            this.subType = subType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPartyName() {
            return partyName;
        }

        public void setPartyName(String partyName) {
            this.partyName = partyName;
        }

        public String getcName() {
            return cName;
        }

        public void setcName(String cName) {
            this.cName = cName;
        }
    }
}
