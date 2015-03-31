package com.next.eswaraj.admin.jsf.dto;

import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.extended.ComplaintDepartmentSearchResult;

public class ComplaintSearchResultDto {

    private ComplaintDepartmentSearchResult complaintSearchResult;
    private Category rootCategory;
    private Category subCategory;

    public ComplaintSearchResultDto() {
    }

    public ComplaintSearchResultDto(ComplaintDepartmentSearchResult complaintSearchResult, Category rootCategory, Category subCategory) {
        super();
        this.complaintSearchResult = complaintSearchResult;
        this.rootCategory = rootCategory;
        this.subCategory = subCategory;
    }

    public ComplaintDepartmentSearchResult getComplaintSearchResult() {
        return complaintSearchResult;
    }

    public void setComplaintSearchResult(ComplaintDepartmentSearchResult complaintSearchResult) {
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
