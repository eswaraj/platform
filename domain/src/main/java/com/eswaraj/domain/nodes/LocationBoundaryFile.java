package com.eswaraj.domain.nodes;

import java.util.Date;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.eswaraj.domain.base.BaseNode;

/**
 * Boundary file of a Location
 * @author ravi
 * @date May 25, 2014
 *
 */
@NodeEntity
@TypeAlias("LocationBoundaryFile")
public class LocationBoundaryFile extends BaseNode {

	@Indexed
	private String fileNameAndPath;
    @RelatedTo(type = "FOR")
	private Location location;
	private Date uploadDate;
	private String status;
    private boolean active;
    private String originalFileName;
    private Long totalTimeToProcessMs;

	public String getFileNameAndPath() {
		return fileNameAndPath;
	}
	public void setFileNameAndPath(String fileNameAndPath) {
		this.fileNameAndPath = fileNameAndPath;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public Long getTotalTimeToProcessMs() {
        return totalTimeToProcessMs;
    }

    public void setTotalTimeToProcessMs(Long totalTimeToProcessMs) {
        this.totalTimeToProcessMs = totalTimeToProcessMs;
    }
    @Override
    public String toString() {
        return "LocationBoundaryFile [fileNameAndPath=" + fileNameAndPath + ", location=" + location + ", uploadDate=" + uploadDate + ", status=" + status + ", active=" + active + ", id=" + id + "]";
    }
	
	
}
