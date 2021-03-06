package com.eswaraj.domain.nodes;

import java.util.Date;

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
    private Date expectedStartDate;
    private Date actualStartDate;
    private Date expectedEndDate;
    private Date actualEndDate;
    private Double expectedCost;
    private Double actualCost;
    private Person createdBy;
    private Location location;

}
