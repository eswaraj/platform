package com.eswaraj.core.service;

import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.v1.TimelineItemDto;

public interface TimelineService {

    
    List<TimelineItemDto> getTimelineItemsOfPoliticalAdmin(Long politicalAdminId, int start, int size) throws ApplicationException;

    List<TimelineItemDto> getTimelineItemsOfLocation(Long locationId, int start, int size) throws ApplicationException;

    List<TimelineItemDto> getTimelineItemsOfPromise(Long promiseId, int start, int size) throws ApplicationException;
}
