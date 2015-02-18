package com.next.eswaraj.admin.jsf.bean;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.TimelineItem;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class TimelineItemBean extends BaseBean {

    private boolean showList;
    private List<TimelineItem> timelineItems;
    private TimelineItem selectedTimelineItem;

    @Autowired
    private AdminService adminService;
    
    @PostConstruct
    public void init() {
        try {
            showList = true;
            timelineItems = adminService.getTimelineItems(0, 20);
        } catch (ApplicationException e) {
            sendErrorMessage("Error", e.getMessage());
        }
    }

    public void createTimelineItem() {
        selectedTimelineItem = new TimelineItem();
        showList = false;
    }

    public void cancel() {
        selectedTimelineItem = new TimelineItem();
        showList = true;
    }

    public void saveTimelineItem() {
        try {
            selectedTimelineItem = adminService.saveTimelineItem(selectedTimelineItem);
            timelineItems = adminService.getTimelineItems(0, 20);
            showList = true;
        } catch (Exception e) {
            sendErrorMessage("Error", "Unable to save Timeline Item", e);
        }

    }

    public boolean isShowList() {
        return showList;
    }

    public void setShowList(boolean showList) {
        this.showList = showList;
    }

    public TimelineItem getSelectedTimelineItem() {
        return selectedTimelineItem;
    }

    public void setSelectedTimelineItem(TimelineItem selectedTimelineItem) {
        this.selectedTimelineItem = new TimelineItem();
        BeanUtils.copyProperties(selectedTimelineItem, this.selectedTimelineItem);
        showList = false;
    }

    public List<TimelineItem> getTimelineItems() {
        return timelineItems;
    }

    public void setTimelineItems(List<TimelineItem> timelineItems) {
        this.timelineItems = timelineItems;
    }

}
