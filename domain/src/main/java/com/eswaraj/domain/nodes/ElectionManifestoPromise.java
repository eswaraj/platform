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
@TypeAlias("ElectionManifestoPromise")
public class ElectionManifestoPromise extends BaseNode {

    private String title;
	
    private String description;

    @RelatedTo(type = "OF_ELECTION_MANIFESTO")
    private ElectionManifesto electionManifesto;


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

    public ElectionManifesto getElectionManifesto() {
        return electionManifesto;
    }

    public void setElectionManifesto(ElectionManifesto electionManifesto) {
        this.electionManifesto = electionManifesto;
    }

}
