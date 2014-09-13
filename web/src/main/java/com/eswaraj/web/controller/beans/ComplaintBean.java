package com.eswaraj.web.controller.beans;

import java.util.List;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;


public class ComplaintBean extends BaseBean {

    private static final long serialVersionUID = 1L;
    private Long complaintTime;
    private String complaintTimeIso;
    private String title;
    private String description;
    private Double lattitude;
    private Double longitude;
    private String status;
    private String categoryTitle;
    private CategoryBean[] categories;
    private LocationBean[] locations;
    private PhotoBean[] photos;
    private PersonBean loggedBy;
    
    public Long getComplaintTime() {
        return complaintTime;
    }

    public void setComplaintTime(Long complaintTime) {
        this.complaintTime = complaintTime;
    }

    public String getComplaintTimeIso() {
        return complaintTimeIso;
    }

    public void setComplaintTimeIso(String complaintTimeIso) {
        this.complaintTimeIso = complaintTimeIso;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLattitude() {
        return lattitude;
    }

    public void setLattitude(Double lattitude) {
        this.lattitude = lattitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public CategoryBean[] getCategories() {
        return categories;
    }

    public void setCategories(CategoryBean[] categories) {
        this.categories = categories;
    }

    public LocationBean[] getLocations() {
        return locations;
    }

    public void setLocations(LocationBean[] locations) {
        this.locations = locations;
    }

    public PhotoBean[] getPhotos() {
        return photos;
    }

    public void setPhotos(PhotoBean[] photos) {
        this.photos = photos;
    }

    public PersonBean getLoggedBy() {
        return loggedBy;
    }

    public void setLoggedBy(PersonBean loggedBy) {
        this.loggedBy = loggedBy;
    }

    public static void main(String[] args) {
        String data = "[{'complaintTime':1407358366192,'title':'test','description':'test','lattitude':51.5708933,'longitude':-0.3732831,'status':'PENDING','categoryTitle':'Leaking Water Pipes','categories':[{'externalId':null,'headerImageurl':'','imageUrl':'','name':'Leaking Water Pipes','videoUrl':'','root':false},{'externalId':null,'headerImageurl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/banner_water.png','imageUrl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ic_issue_water.png','name':'Water','videoUrl':'https://www.youtube.com/watch?v=gB-JyWs_PQA','root':true}],'locations':[{'externalId':null,'name':'India'}]},{'complaintTime':1407358697635,'title':'helli','description':'helli','lattitude':51.57093,'longitude':-0.3731232,'status':'PENDING','categoryTitle':' Enough these power cuts, increase power generation capacity.','categories':[{'externalId':null,'headerImageurl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/banner_electricity.png','imageUrl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ic_issue_electricity.png','name':'Electricity','videoUrl':'https://www.youtube.com/watch?v=gB-JyWs_PQA','root':true},{'externalId':null,'headerImageurl':'','imageUrl':'','name':' Enough these power cuts, increase power generation capacity.','videoUrl':'','root':false}],'locations':[{'externalId':null,'name':'India'}]},{'complaintTime':1407358747706,'title':'ravi','description':'ravi','lattitude':51.57093,'longitude':-0.3731232,'status':'PENDING','categoryTitle':' should not there be a court awareness day , once in 3 months , where citizens should be encouraged to review there courts , to check that they are happy with service of the staff.','categories':[{'externalId':null,'headerImageurl':'','imageUrl':'','name':' should not there be a court awareness day , once in 3 months , where citizens should be encouraged to review there courts , to check that they are happy with service of the staff.','videoUrl':'','root':false},{'externalId':null,'headerImageurl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ichttps://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/banner_law.png_issue_law.png','imageUrl':' https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ic_issue_law.png','name':'Law & Order','videoUrl':'https://www.youtube.com/watch?v=9pc5dP-3xLk','root':true}],'locations':[{'externalId':null,'name':'India'}]},{'complaintTime':1407358887037,'title':'re','description':'re','lattitude':51.570924,'longitude':-0.3732533,'status':'PENDING','categoryTitle':' No 24x7 power in my area.','categories':[{'externalId':null,'headerImageurl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/banner_electricity.png','imageUrl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ic_issue_electricity.png','name':'Electricity','videoUrl':'https://www.youtube.com/watch?v=gB-JyWs_PQA','root':true},{'externalId':null,'headerImageurl':'','imageUrl':'','name':' No 24x7 power in my area.','videoUrl':'','root':false}],'locations':[{'externalId':null,'name':'India'}]},{'complaintTime':1407359500723,'title':'avghh','description':'avghh','lattitude':51.5708991,'longitude':-0.3732145,'status':'PENDING','categoryTitle':'No Munciplaity Water at homes','categories':[{'externalId':null,'headerImageurl':'','imageUrl':'','name':'No Munciplaity Water at homes','videoUrl':'','root':false},{'externalId':null,'headerImageurl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/banner_water.png','imageUrl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ic_issue_water.png','name':'Water','videoUrl':'https://www.youtube.com/watch?v=gB-JyWs_PQA','root':true}],'locations':[{'externalId':null,'name':'India'}]},{'complaintTime':1407390065945,'title':'','description':'','lattitude':51.5708786,'longitude':-0.3731828,'status':'PENDING','categoryTitle':'Frequent blasts in transformers in this area','categories':[{'externalId':null,'headerImageurl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/banner_electricity.png','imageUrl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ic_issue_electricity.png','name':'Electricity','videoUrl':'https://www.youtube.com/watch?v=gB-JyWs_PQA','root':true},{'externalId':null,'headerImageurl':'','imageUrl':'','name':'Frequent blasts in transformers in this area','videoUrl':'','root':false}],'locations':[{'externalId':null,'name':'India'}]},{'complaintTime':1407390112673,'title':'','description':'','lattitude':12.9950756,'longitude':77.6718386,'status':'PENDING','categoryTitle':'No Munciplaity Water at homes','categories':[{'externalId':null,'headerImageurl':'','imageUrl':'','name':'No Munciplaity Water at homes','videoUrl':'','root':false},{'externalId':null,'headerImageurl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/banner_water.png','imageUrl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ic_issue_water.png','name':'Water','videoUrl':'https://www.youtube.com/watch?v=gB-JyWs_PQA','root':true}],'locations':[{'externalId':null,'name':'India'}]},{'complaintTime':1407390139193,'title':'','description':'','lattitude':12.998053,'longitude':77.6710757,'status':'PENDING','categoryTitle':' No close by police station in my area.','categories':[{'externalId':null,'headerImageurl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ichttps://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/banner_law.png_issue_law.png','imageUrl':' https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ic_issue_law.png','name':'Law & Order','videoUrl':'https://www.youtube.com/watch?v=9pc5dP-3xLk','root':true},{'externalId':null,'headerImageurl':'','imageUrl':'','name':' No close by police station in my area.','videoUrl':'','root':false}],'locations':[{'externalId':null,'name':'India'}]},{'complaintTime':1407390206278,'title':'','description':'','lattitude':12.9976129,'longitude':77.6705857,'status':'PENDING','categoryTitle':'No Munciplaity Water at homes','categories':[{'externalId':null,'headerImageurl':'','imageUrl':'','name':'No Munciplaity Water at homes','videoUrl':'','root':false},{'externalId':null,'headerImageurl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/banner_water.png','imageUrl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ic_issue_water.png','name':'Water','videoUrl':'https://www.youtube.com/watch?v=gB-JyWs_PQA','root':true}],'locations':[{'externalId':null,'name':'India'}]},{'complaintTime':1407390300761,'title':'','description':'','lattitude':12.9979304,'longitude':77.6704754,'status':'PENDING','categoryTitle':'Poor calliberation of water meters','categories':[{'externalId':null,'headerImageurl':'','imageUrl':'','name':'Poor calliberation of water meters','videoUrl':'','root':false},{'externalId':null,'headerImageurl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/banner_water.png','imageUrl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ic_issue_water.png','name':'Water','videoUrl':'https://www.youtube.com/watch?v=gB-JyWs_PQA','root':true}],'locations':[{'externalId':null,'name':'India'}]},{'complaintTime':1407390501715,'title':'','description':'','lattitude':51.570898,'longitude':-0.3731693,'status':'PENDING','categoryTitle':'Unaffordable road taxes.','categories':[{'externalId':null,'headerImageurl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/banner_road.png','imageUrl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ic_issue_road.png','name':'Roads','videoUrl':'https://www.youtube.com/watch?v=mnYqM5W-5t0','root':true},{'externalId':null,'headerImageurl':'','imageUrl':'','name':'Unaffordable road taxes.','videoUrl':'','root':false}],'locations':[{'externalId':null,'name':'India'}]},{'complaintTime':1407390586973,'title':'','description':'','lattitude':12.9978721,'longitude':77.6713077,'status':'PENDING','categoryTitle':'No Munciplaity Water at homes','categories':[{'externalId':null,'headerImageurl':'','imageUrl':'','name':'No Munciplaity Water at homes','videoUrl':'','root':false},{'externalId':null,'headerImageurl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/banner_water.png','imageUrl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ic_issue_water.png','name':'Water','videoUrl':'https://www.youtube.com/watch?v=gB-JyWs_PQA','root':true}],'locations':[{'externalId':null,'name':'India'}]},{'complaintTime':1407390601675,'title':'','description':'','lattitude':12.9978721,'longitude':77.6713077,'status':'PENDING','categoryTitle':'No Munciplaity Water at homes','categories':[{'externalId':null,'headerImageurl':'','imageUrl':'','name':'No Munciplaity Water at homes','videoUrl':'','root':false},{'externalId':null,'headerImageurl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/banner_water.png','imageUrl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ic_issue_water.png','name':'Water','videoUrl':'https://www.youtube.com/watch?v=gB-JyWs_PQA','root':true}],'locations':[{'externalId':null,'name':'India'}]},{'complaintTime':1407402402020,'title':'','description':'','lattitude':12.9344088,'longitude':77.6957705,'status':'PENDING','categoryTitle':'No Munciplaity Water at homes','categories':[{'externalId':null,'headerImageurl':'','imageUrl':'','name':'No Munciplaity Water at homes','videoUrl':'','root':false},{'externalId':null,'headerImageurl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/banner_water.png','imageUrl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ic_issue_water.png','name':'Water','videoUrl':'https://www.youtube.com/watch?v=gB-JyWs_PQA','root':true}],'locations':[{'externalId':null,'name':'India'}]},{'complaintTime':1407407549746,'title':'','description':'','lattitude':12.9352893,'longitude':77.6960779,'status':'PENDING','categoryTitle':'No Munciplaity Water at homes','categories':[{'externalId':null,'headerImageurl':'','imageUrl':'','name':'No Munciplaity Water at homes','videoUrl':'','root':false},{'externalId':null,'headerImageurl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/banner_water.png','imageUrl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ic_issue_water.png','name':'Water','videoUrl':'https://www.youtube.com/watch?v=gB-JyWs_PQA','root':true}],'locations':[{'externalId':null,'name':'India'}]},{'complaintTime':1407421062129,'title':'','description':'','lattitude':12.9344538,'longitude':77.6952394,'status':'PENDING','categoryTitle':'No Munciplaity Water at homes','categories':[{'externalId':null,'headerImageurl':'','imageUrl':'','name':'No Munciplaity Water at homes','videoUrl':'','root':false},{'externalId':null,'headerImageurl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/banner_water.png','imageUrl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ic_issue_water.png','name':'Water','videoUrl':'https://www.youtube.com/watch?v=gB-JyWs_PQA','root':true}],'locations':[{'externalId':null,'name':'India'}]},{'complaintTime':1407421070134,'title':'','description':'','lattitude':12.9344552,'longitude':77.6952461,'status':'PENDING','categoryTitle':' No close by police station in my area.','categories':[{'externalId':null,'headerImageurl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ichttps://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/banner_law.png_issue_law.png','imageUrl':' https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ic_issue_law.png','name':'Law & Order','videoUrl':'https://www.youtube.com/watch?v=9pc5dP-3xLk','root':true},{'externalId':null,'headerImageurl':'','imageUrl':'','name':' No close by police station in my area.','videoUrl':'','root':false}],'locations':[{'externalId':null,'name':'India'}]},{'complaintTime':1407489340401,'title':'','description':'','lattitude':12.9344768,'longitude':77.6952955,'status':'PENDING','categoryTitle':'No Munciplaity Water at homes','categories':[{'externalId':null,'headerImageurl':'','imageUrl':'','name':'No Munciplaity Water at homes','videoUrl':'','root':false},{'externalId':null,'headerImageurl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/banner_water.png','imageUrl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ic_issue_water.png','name':'Water','videoUrl':'https://www.youtube.com/watch?v=gB-JyWs_PQA','root':true}],'locations':[{'externalId':null,'name':'India'}]},{'complaintTime':1407489908549,'title':'','description':'','lattitude':12.9344671,'longitude':77.6952616,'status':'PENDING','categoryTitle':'No Munciplaity Water at homes','categories':[{'externalId':null,'headerImageurl':'','imageUrl':'','name':'No Munciplaity Water at homes','videoUrl':'','root':false},{'externalId':null,'headerImageurl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/banner_water.png','imageUrl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ic_issue_water.png','name':'Water','videoUrl':'https://www.youtube.com/watch?v=gB-JyWs_PQA','root':true}],'locations':[{'externalId':null,'name':'India'}]},{'complaintTime':1407491729387,'title':'','description':'','lattitude':12.9344745,'longitude':77.6951964,'status':'PENDING','categoryTitle':'No tar/pucca roads in this area.','categories':[{'externalId':null,'headerImageurl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/banner_road.png','imageUrl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ic_issue_road.png','name':'Roads','videoUrl':'https://www.youtube.com/watch?v=mnYqM5W-5t0','root':true},{'externalId':null,'headerImageurl':'','imageUrl':'','name':'No tar/pucca roads in this area.','videoUrl':'','root':false}],'locations':[{'externalId':null,'name':'India'}]},{'complaintTime':1407549762683,'title':'','description':'','lattitude':12.9981268,'longitude':77.6711796,'status':'PENDING','categoryTitle':'No Munciplaity Water at homes','categories':[{'externalId':null,'headerImageurl':'','imageUrl':'','name':'No Munciplaity Water at homes','videoUrl':'','root':false},{'externalId':null,'headerImageurl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/banner_water.png','imageUrl':'https://s3-us-west-2.amazonaws.com/eswaraj-dev/mobile/images/ic_issue_water.png','name':'Water','videoUrl':'https://www.youtube.com/watch?v=gB-JyWs_PQA','root':true}],'locations':[{'externalId':null,'name':'India'}]}]";
        Gson gson = new Gson();
        List<ComplaintBean> list = gson.fromJson(data, new TypeToken<List<ComplaintBean>>() {
        }.getType());
        for (ComplaintBean oneComplaintBean : list) {
            System.out.println("" + oneComplaintBean.getCategoryTitle() + " , " + oneComplaintBean.getCategories()[0].getName() + ", " + oneComplaintBean.getLocations()[0].getName());
        }
    }
}
