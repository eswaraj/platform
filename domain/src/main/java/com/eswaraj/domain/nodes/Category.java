package com.eswaraj.domain.nodes;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.eswaraj.domain.base.BaseNode;

/**
 * Category of a complaint 
 * @author ravi
 * @date Jan 18, 2014
 *
 */
@NodeEntity
public class Category extends BaseNode{

	private String name;
	private String description;
	@RelatedTo(type="BELONGS_TO")
    private Category parentCategory;
	private String imageUrl;
	private boolean root;
	private String headerImageUrl;
	private String videoUrl;
    private String urlIdentifier;
    private String color;
    @RelatedTo(type = "SYSTEM_CATEGORY")
    @Fetch
    private SystemCategory systemCategory;
	    
    public Category() {}
    public Category(String name) {
    	this.name = name;
    }
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Category getParentCategory() {
		return parentCategory;
	}
	public void setParentCategory(Category parentCategory) {
		this.parentCategory = parentCategory;
	}
	
	public boolean isRoot() {
		return root;
	}
	public void setRoot(boolean root) {
		this.root = root;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getHeaderImageUrl() {
		return headerImageUrl;
	}
	public void setHeaderImageUrl(String headerImageUrl) {
		this.headerImageUrl = headerImageUrl;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

    public String getUrlIdentifier() {
        return urlIdentifier;
    }

    public void setUrlIdentifier(String urlIdentifier) {
        this.urlIdentifier = urlIdentifier;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public SystemCategory getSystemCategory() {
        return systemCategory;
    }

    public void setSystemCategory(SystemCategory systemCategory) {
        this.systemCategory = systemCategory;
    }
    @Override
	public String toString() {
		return "Category [name=" + name + ", description=" + description
				+ ", parentCategory=" + parentCategory + ", imageUrl="
				+ imageUrl + ", root=" + root + ", headerImageUrl="
				+ headerImageUrl + ", videoUrl=" + videoUrl + ", id=" + id
				+ "]";
	}
	
}
