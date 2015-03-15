package com.next.eswaraj.admin.jsf.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.cache.CategoryCache;
import com.eswaraj.cache.CounterCache;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Comment;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.Photo;
import com.eswaraj.domain.nodes.PoliticalAdminComplaintStatus;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.extended.ComplaintSearchResult;
import com.eswaraj.domain.nodes.relationships.ComplaintPoliticalAdmin;
import com.eswaraj.messaging.dto.CommentSavedMessage;
import com.eswaraj.messaging.dto.ComplaintViewedByPoliticalAdminMessage;
import com.eswaraj.queue.service.QueueService;
import com.eswaraj.web.dto.UserDto;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.next.eswaraj.admin.jsf.dto.ComplaintSearchResultDto;
import com.next.eswaraj.admin.service.AdminService;
import com.next.eswaraj.web.session.SessionUtil;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
public class ComplaintsBean extends BaseBean {

    @Autowired
    private AdminService adminService;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private QueueService queueService;

    @Autowired
    private CategoryCache categoryCache;

    @Autowired
    private CounterCache counterCache;

    @Autowired
    private LoginBean loginBean;

    private PieChartModel categoryPieChartModel;

    private LineChartModel dailyLineChartModel;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<ComplaintSearchResultDto> complaints;

    private ComplaintSearchResultDto selectedComplaint;

    private boolean showList = true;

    private MapModel mapModel;

    private List<Photo> complaintPhotos;

    private List<String> images;

    private List<Person> complaintCreators;

    private List<Comment> complaintComments;

    private Map<Long, Category> categoryMap = new HashMap<Long, Category>();

    private String comment;

    private String updatedStatus;

    @PostConstruct
    public void init() {
        try {
            List<Category> allCategories = adminService.getAllcategories();
            for (Category oneCategory : allCategories) {
                categoryMap.put(oneCategory.getId(), oneCategory);
            }
            refreshComplaintList();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void cancel() {
        showList = true;
    }

    public void updateComplaint(){
        try{

            ComplaintPoliticalAdmin complaintPoliticalAdmin = selectedComplaint.getComplaintSearchResult().getComplaintPoliticalAdmin();
            if (updatedStatus != null && !updatedStatus.trim().equals("") && complaintPoliticalAdmin.getStatus() != null && !complaintPoliticalAdmin.getStatus().name().equals(updatedStatus)) {
                complaintPoliticalAdmin.setStatus(PoliticalAdminComplaintStatus.valueOf(updatedStatus));
                complaintPoliticalAdmin = adminService.saveComplaintPoliticalAdmin(complaintPoliticalAdmin);

            }

            Complaint complaint = selectedComplaint.getComplaintSearchResult().getComplaint();
            PoliticalBodyAdmin selectedPoliticalBodyAdmin = loginBean.getSelectedPoliticalBodyAdmin();

            if (comment != null && !comment.trim().equals("") && selectedPoliticalBodyAdmin != null) {
                HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                UserDto userDto = sessionUtil.getLoggedInUserFromSession(httpServletRequest);
                Comment savedComment = adminService.saveComplaintComment(complaint, selectedPoliticalBodyAdmin, userDto.getPerson().getId(), comment);
                // Send Complaint Comment Message
                CommentSavedMessage commentSavedMessage = new CommentSavedMessage();
                commentSavedMessage.setCommentId(savedComment.getId());
                commentSavedMessage.setComplaintId(complaint.getId());
                commentSavedMessage.setPersonId(userDto.getPerson().getId());
                commentSavedMessage.setPoliticalAdminId(selectedPoliticalBodyAdmin.getId());
                queueService.sendCommentSavedMessage(commentSavedMessage);

                comment = "";
            }
            sendInfoMessage("Success", "Updated Succesfully");
            showList = true;
            refreshComplaintList();
        }catch(Exception ex){
            sendErrorMessage("Error", ex.getMessage());
            logger.error("Unable to save Complaint", ex);
        }
    }

    public List<ComplaintSearchResultDto> getComplaints() {
        if (complaints == null || complaints.isEmpty()) {
            init();
        }
        return complaints;
    }


    private void refreshComplaintList() {
        loginBean.refreshLoginRoles();
        PoliticalBodyAdmin selectedPoliticalBodyAdmin = loginBean.getSelectedPoliticalBodyAdmin();

        if (selectedPoliticalBodyAdmin == null) {

        } else {
            try {
                List<ComplaintSearchResult> complaints = adminService.getPoliticalAdminComplaintsAll(selectedPoliticalBodyAdmin.getId());
                this.complaints = new ArrayList<ComplaintSearchResultDto>();
                for (ComplaintSearchResult oneComplaintSearchResult : complaints) {
                    Category rootCategory = null;
                    Category subCategory = null;
                    for (Category oneCategory : oneComplaintSearchResult.getComplaint().getCategories()) {
                        Category loadedCategory = categoryMap.get(oneCategory.getId());
                        if (loadedCategory != null) {
                            if(loadedCategory.isRoot()){
                                rootCategory = loadedCategory;
                            }else{
                                subCategory = loadedCategory;
                            }
                        }
                    }
                    ComplaintSearchResultDto oneComplaintSearchResultDto = new ComplaintSearchResultDto(oneComplaintSearchResult, rootCategory, subCategory);
                    this.complaints.add(oneComplaintSearchResultDto);

                }
            } catch (ApplicationException e) {
                e.printStackTrace();
            }
        }

        try {
            categoryPieChartModel = new PieChartModel();
            JsonArray jsonArray = categoryCache.getAllCategoryStatsForLocation(selectedPoliticalBodyAdmin.getLocation().getId());
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject oneJsonObject = jsonArray.get(i).getAsJsonObject();
                categoryPieChartModel.set(oneJsonObject.get("name").getAsString(), oneJsonObject.get("locationCount").getAsLong());
            }
            categoryPieChartModel.setTitle("Category Wise");
            categoryPieChartModel.setLegendPosition("w");
            categoryPieChartModel.setShadow(true);
            categoryPieChartModel.setShowDataLabels(true);
            categoryPieChartModel.setMouseoverHighlight(true);

            // Linear chart
            JsonObject jsonObject = counterCache.getLast30DayLocationCounters(selectedPoliticalBodyAdmin.getLocation().getId(), new Date());
            JsonArray dailyCounterJsonArray = jsonObject.get("dayWise").getAsJsonArray();
            dailyLineChartModel = new LineChartModel();
            ChartSeries daily = new ChartSeries();
            daily.setLabel("Day wise");
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < dailyCounterJsonArray.size(); i++) {
                JsonObject oneJsonObject = dailyCounterJsonArray.get(i).getAsJsonObject();
                for (Entry<String, JsonElement> oneEntry : oneJsonObject.entrySet()) {
                    Date date = simpleDateFormat1.parse(oneEntry.getKey().replace(".", ""));
                    daily.set(simpleDateFormat2.format(date), oneEntry.getValue().getAsLong());
                    System.out.println(simpleDateFormat2.format(date) + "=" + oneEntry.getValue().getAsLong());

                    // daily.set((i + 1), oneEntry.getValue().getAsLong());
                }
            }

            dailyLineChartModel.getAxis(AxisType.Y).setLabel("Number of Complaints");
            DateAxis dateAxis = new DateAxis("Dates");
            dateAxis.setTickAngle(-50);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -30);
            dateAxis.setMax(simpleDateFormat2.format(cal.getTime()));
            dateAxis.setTickFormat("%b %#d, %y");

            dailyLineChartModel.getAxes().put(AxisType.X, dateAxis);

            dailyLineChartModel.addSeries(daily);
        } catch (ApplicationException e) {
            sendErrorMessage("Error", e.getMessage());
            e.printStackTrace();
        } catch (ParseException e) {
            sendErrorMessage("Error", e.getMessage());
            e.printStackTrace();
        }
    }

    public void setComplaints(List<ComplaintSearchResultDto> complaints) {
        this.complaints = complaints;
    }

    public ComplaintSearchResultDto getSelectedComplaint() {
        return selectedComplaint;
    }

    public void refreshComplaints() {
        refreshComplaintList();
    }

    public void setSelectedComplaint(ComplaintSearchResultDto complaintSearchResult) {
        this.selectedComplaint = complaintSearchResult;
        showList = false;
        mapModel = new DefaultMapModel();
        LatLng coord1 = new LatLng(complaintSearchResult.getComplaintSearchResult().getComplaint().getLattitude(), complaintSearchResult.getComplaintSearchResult().getComplaint().getLongitude());
        PoliticalBodyAdmin selectedPoliticalBodyAdmin = loginBean.getSelectedPoliticalBodyAdmin();
        // Basic marker
        mapModel.addOverlay(new Marker(coord1, complaintSearchResult.getComplaintSearchResult().getComplaint().getTitle()));
        if (complaintSearchResult.getComplaintSearchResult().getComplaintPoliticalAdmin().getStatus() != null) {
            updatedStatus = complaintSearchResult.getComplaintSearchResult().getComplaintPoliticalAdmin().getStatus().name();
        } else {
            updatedStatus = "";
        }
        try {
            images = new ArrayList<String>();
            complaintPhotos = adminService.getComplaintPhotos(complaintSearchResult.getComplaintSearchResult().getComplaint().getId());
            complaintCreators = adminService.getComplaintCreators(complaintSearchResult.getComplaintSearchResult().getComplaint().getId());
            complaintComments = adminService.getComplaintComments(complaintSearchResult.getComplaintSearchResult().getComplaint().getId());
            for (Photo onePhoto : complaintPhotos) {
                images.add(onePhoto.getOrgUrl());
            }
            if (!complaintSearchResult.getComplaintSearchResult().getComplaintPoliticalAdmin().isViewed()) {
                adminService.markComplaintViewed(complaintSearchResult.getComplaintSearchResult().getComplaint().getId(), selectedPoliticalBodyAdmin.getId());
                ComplaintViewedByPoliticalAdminMessage complaintViewedByPoliticalAdminMessage = new ComplaintViewedByPoliticalAdminMessage();
                complaintViewedByPoliticalAdminMessage.setComplaintId(complaintSearchResult.getComplaintSearchResult().getComplaint().getId());
                complaintViewedByPoliticalAdminMessage.setPersonId(selectedPoliticalBodyAdmin.getPerson().getId());
                complaintViewedByPoliticalAdminMessage.setPoliticalAdminId(selectedPoliticalBodyAdmin.getId());
                queueService.sendComplaintViewedByPoliticalLeaderMessage(complaintViewedByPoliticalAdminMessage);
            }

        } catch (ApplicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean isShowList() {
        return showList;
    }

    public void setShowList(boolean showList) {
        this.showList = showList;
    }

    public MapModel getMapModel() {
        return mapModel;
    }

    public void setMapModel(MapModel mapModel) {
        this.mapModel = mapModel;
    }

    public List<Photo> getComplaintPhotos() {
        return complaintPhotos;
    }

    public void setComplaintPhotos(List<Photo> complaintPhotos) {
        this.complaintPhotos = complaintPhotos;
    }

    public List<Person> getComplaintCreators() {
        return complaintCreators;
    }

    public void setComplaintCreators(List<Person> complaintCreators) {
        this.complaintCreators = complaintCreators;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<Comment> getComplaintComments() {
        return complaintComments;
    }

    public void setComplaintComments(List<Comment> complaintComments) {
        this.complaintComments = complaintComments;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUpdatedStatus() {
        return updatedStatus;
    }

    public void setUpdatedStatus(String updatedStatus) {
        this.updatedStatus = updatedStatus;
    }

    public PieChartModel getCategoryPieChartModel() {
        return categoryPieChartModel;
    }

    public void setCategoryPieChartModel(PieChartModel categoryPieChartModel) {
        this.categoryPieChartModel = categoryPieChartModel;
    }

    public LineChartModel getDailyLineChartModel() {
        return dailyLineChartModel;
    }

    public void setDailyLineChartModel(LineChartModel dailyLineChartModel) {
        this.dailyLineChartModel = dailyLineChartModel;
    }

}
