package com.eswaraj.domain.nodes;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.NodeEntity;

import com.eswaraj.domain.base.BaseNode;
import com.eswaraj.domain.base.DocumentLanguage;

/**
 * Photo of the complaint and/or location
 * 
 * @author ravi
 * @date Jan 30, 2015
 *
 */
@NodeEntity
@TypeAlias("Document")
public class Document extends BaseNode {

    private String url;

    private DocumentLanguage language;
	
    private DocumentType type;

    public enum DocumentType {
        Pdf, MsDoc
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

    public DocumentLanguage getLanguage() {
        return language;
    }

    public void setLanguage(DocumentLanguage language) {
        this.language = language;
    }
	
}
