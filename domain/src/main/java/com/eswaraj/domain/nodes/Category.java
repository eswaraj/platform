package com.eswaraj.domain.nodes;

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
	private boolean root;
	    
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
	@Override
	public String toString() {
		return "Category [name=" + name + ", description=" + description + ", parentCategory=" + parentCategory + ", root=" + root + ", id=" + id + "]";
	}
	
}
