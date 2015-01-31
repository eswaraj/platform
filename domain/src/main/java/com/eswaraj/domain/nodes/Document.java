package com.eswaraj.domain.nodes;

import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.NodeEntity;

import com.eswaraj.domain.base.BaseNode;

/**
 * Photo of the complaint and/or location
 * 
 * @author ravi
 * @date Jan 30, 2015
 *
 */
@NodeEntity
public class Document extends BaseNode {

    private String url;
	
    @GraphProperty(propertyType = Enum.class)
    private DocumentType type;

    public enum DocumentType {
        Pdf, doc
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }
	
}
