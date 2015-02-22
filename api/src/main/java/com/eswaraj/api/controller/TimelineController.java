package com.eswaraj.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.TimelineService;
import com.eswaraj.web.dto.v1.TimelineItemDto;

@Controller
public class TimelineController extends BaseController {

    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private TimelineService timelineService;


    @RequestMapping(value = "/api/v0/timeline/leader/{politicalAdminId}", method = RequestMethod.GET)
    public @ResponseBody List<TimelineItemDto> getTimelineItemsOfPoliticalAdmin(ModelAndView mv, HttpServletRequest httpServletRequest, @PathVariable Long politicalAdminId)
            throws ApplicationException {
        int start = getIntParameter(httpServletRequest, "start", 0);
        int size = getIntParameter(httpServletRequest, "size", 10);
        return timelineService.getTimelineItemsOfPoliticalAdmin(politicalAdminId, start, size);
    }

    @RequestMapping(value = "/api/v0/timeline/location/{locationId}", method = RequestMethod.GET)
    public @ResponseBody List<TimelineItemDto> getTimelineItemsOfLocation(ModelAndView mv, HttpServletRequest httpServletRequest, @PathVariable Long locationId) throws ApplicationException {
        int start = getIntParameter(httpServletRequest, "start", 0);
        int size = getIntParameter(httpServletRequest, "size", 10);
        return timelineService.getTimelineItemsOfLocation(locationId, start, size);
    }

}
