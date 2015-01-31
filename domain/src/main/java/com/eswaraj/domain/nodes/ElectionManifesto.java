package com.eswaraj.domain.nodes;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.eswaraj.domain.base.BaseNode;

/**
 * Election Types
 * 
 * @author ravi
 * @date Jan 28, 2015
 *
 */
@NodeEntity
@TypeAlias("ElectionManifesto")
public class ElectionManifesto extends BaseNode {

	private String name;
	
    private String description;

    @RelatedTo(type = "OF_ELECTION")
    private Election election;

    @RelatedTo(type = "OF_PARTY")
    private Party party;

    @RelatedTo(type = "MANIFESTO_DOCUMENT")
    private Document document;

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

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

}
