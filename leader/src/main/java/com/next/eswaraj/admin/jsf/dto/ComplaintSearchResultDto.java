package com.next.eswaraj.admin.jsf.dto;

import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.extended.ComplaintSearchResult;

public class ComplaintSearchResultDto {

    private ComplaintSearchResult complaintSearchResult;
    private Category rootCategory;
    private Category subCategory;

    public ComplaintSearchResultDto() {
    }

    public ComplaintSearchResultDto(ComplaintSearchResult complaintSearchResult, Category rootCategory, Category subCategory) {
        super();
        this.complaintSearchResult = complaintSearchResult;
        this.rootCategory = rootCategory;
        this.subCategory = subCategory;
    }

    public ComplaintSearchResult getComplaintSearchResult() {
        return complaintSearchResult;
    }

    public void setComplaintSearchResult(ComplaintSearchResult complaintSearchResult) {
        this.complaintSearchResult = complaintSearchResult;
    }

    public Category getRootCategory() {
        return rootCategory;
    }

    public void setRootCategory(Category rootCategory) {
        this.rootCategory = rootCategory;
    }

    public Category getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(Category subCategory) {
        this.subCategory = subCategory;
    }

}
