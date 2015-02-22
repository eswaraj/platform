package com.eswaraj.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.TimelineService;
import com.eswaraj.domain.nodes.Document;
import com.eswaraj.domain.nodes.TimelineItem;
import com.eswaraj.domain.repo.LocationTimelineItemRepository;
import com.eswaraj.domain.repo.PoliticalAdminTimelineItemRepository;
import com.eswaraj.domain.repo.PromiseTimelineItemRepository;
import com.eswaraj.domain.repo.TimelineItemRepository;
import com.eswaraj.web.dto.v1.TimelineItemDto;

@Service
@Transactional
public class TimelineServiceImpl implements TimelineService {

    @Autowired
    private TimelineItemRepository timelineItemRepository;

    @Autowired
    private PoliticalAdminTimelineItemRepository politicalAdminTimelineItemRepository;

    @Autowired
    private LocationTimelineItemRepository locationTimelineItemRepository;

    @Autowired
    private PromiseTimelineItemRepository promiseTimelineItemRepository;

    @Override
    public List<TimelineItemDto> getTimelineItemsOfPoliticalAdmin(Long politicalAdminId, int start, int size) throws ApplicationException {
        List<TimelineItem> dbTimeLineItems = politicalAdminTimelineItemRepository.getPagedTimelineItemOfPoliticalBodyAdmin(politicalAdminId, start, size);
        return convert(dbTimeLineItems);
    }

    @Override
    public List<TimelineItemDto> getTimelineItemsOfLocation(Long locationId, int start, int size) throws ApplicationException {
        List<TimelineItem> dbTimeLineItems = locationTimelineItemRepository.getPagedTimelineItemOfLocation(locationId, start, size);
        return convert(dbTimeLineItems);
    }

    @Override
    public List<TimelineItemDto> getTimelineItemsOfPromise(Long promiseId, int start, int size) throws ApplicationException {
        List<TimelineItem> dbTimeLineItems = promiseTimelineItemRepository.getPagesTimelineItemOfElectionManifestoPromise(promiseId, start, size);
        return convert(dbTimeLineItems);
    }

    private List<TimelineItemDto> convert(List<TimelineItem> dbTimeLineItems) {
        List<TimelineItemDto> returnList = new ArrayList<TimelineItemDto>();
        for (TimelineItem oneTimelineItem : dbTimeLineItems) {
            TimelineItemDto oneTimelineItemDto = new TimelineItemDto();
            BeanUtils.copyProperties(oneTimelineItem, oneTimelineItemDto);
            addImage(oneTimelineItemDto, oneTimelineItem.getImage1());
            addImage(oneTimelineItemDto, oneTimelineItem.getImage2());
            addImage(oneTimelineItemDto, oneTimelineItem.getImage3());
            addImage(oneTimelineItemDto, oneTimelineItem.getImage4());
            addVideo(oneTimelineItemDto, oneTimelineItem.getYoutubeUrl());
            addDocument(oneTimelineItemDto, oneTimelineItem.getDocument());
            if (StringUtils.isEmpty(oneTimelineItem.getType())) {
                oneTimelineItemDto.setType("generic");
            }
            returnList.add(oneTimelineItemDto);
        }
        return returnList;
    }


    private void addImage(TimelineItemDto oneTimelineItemDto, String image) {
        if (StringUtils.isEmpty(image)) {
            return;
        }
        if (oneTimelineItemDto.getImages() == null) {
            oneTimelineItemDto.setImages(new ArrayList<String>());
        }
        oneTimelineItemDto.getImages().add(image);
    }

    private void addVideo(TimelineItemDto oneTimelineItemDto, String video) {
        if (StringUtils.isEmpty(video)) {
            return;
        }
        if (oneTimelineItemDto.getYoutubeUrl() == null) {
            oneTimelineItemDto.setYoutubeUrl(new ArrayList<String>());
        }
        oneTimelineItemDto.getYoutubeUrl().add(video);
    }

    private void addDocument(TimelineItemDto oneTimelineItemDto, Document document) {
        if (document == null) {
            return;
        }
        if (oneTimelineItemDto.getDocuments() == null) {
            oneTimelineItemDto.setDocuments(new ArrayList<String>());
        }
        oneTimelineItemDto.getDocuments().add(document.getUrl());
    }



}
