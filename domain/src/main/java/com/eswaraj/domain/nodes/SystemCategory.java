package com.eswaraj.domain.nodes;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.NodeEntity;

import com.eswaraj.domain.base.BaseNode;

/**
 * Category of a complaint 
 * @author ravi
 * @date Jan 18, 2014
 *
 */
@NodeEntity
@TypeAlias("SystemCategory")
public class SystemCategory extends BaseNode{

	private String name;
	private String description;
	    
    public SystemCategory() {}
    public SystemCategory(String name) {
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

    @Override
    public String toString() {
        return "SystemCategory [name=" + name + ", description=" + description + "]";
    }
	
}
