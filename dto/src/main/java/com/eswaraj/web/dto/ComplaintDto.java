package com.eswaraj.web.dto;

import java.util.List;



/**
 * DTO for a complaint that has been made by a user.
 * @author anuj
 * @data Jun 22, 2014
 */

public class ComplaintDto extends BaseDto {

	private static final long serialVersionUID = 1L;
	
	private String title;
	private String description;
	private Long categoryId;
	private Long personId;
	private double lattitude;
	private double longitude;
    private Long complaintTime;
    private String status;
	private List<PhotoDto> images;
    private List<PersonDto> createdBy;
    private List<CategoryDto> categories;
	
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
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public Long getPersonId() {
		return personId;
	}
	public void setPersonId(Long personId) {
		this.personId = personId;
	}
	public double getLattitude() {
		return lattitude;
	}
	public void setLattitude(double lattitude) {
		this.lattitude = lattitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public List<PhotoDto> getImages() {
		return images;
	}
	public void setImages(List<PhotoDto> images) {
		this.images = images;
	}

    public Long getComplaintTime() {
        return complaintTime;
    }

    public void setComplaintTime(Long complaintTime) {
        this.complaintTime = complaintTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PersonDto> getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(List<PersonDto> createdBy) {
        this.createdBy = createdBy;
    }

    public List<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDto> categories) {
        this.categories = categories;
    }
    @Override
	public String toString() {
		return "ComplaintDto [title=" + title + ", description=" + description
				+ ", categoryId=" + categoryId + ", personId=" + personId
				+ ", lattitude=" + lattitude + ", longitude=" + longitude
				+ ", id=" + id + "]";
	}
	
}
