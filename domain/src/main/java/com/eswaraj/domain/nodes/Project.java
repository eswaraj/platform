package com.eswaraj.domain.nodes;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.NodeEntity;

import com.eswaraj.domain.base.BaseNode;

/**
 * Highest responsible person for that particular body
 * @author anuj
 * @date Jan 18, 2014
 *
 */
@NodeEntity
@TypeAlias("Project")
public class Project extends BaseNode {

    private String title;
    private String description;
    private String status;
    private Person createdBy;
    private Location location;

}
