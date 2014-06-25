package com.eswaraj.domain.nodes;

import java.util.Set;

import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import org.springframework.data.neo4j.support.index.IndexType;

import com.eswaraj.domain.base.BaseNode;
import com.eswaraj.domain.nodes.division.GeoPoint;

/**
 * Complaint made by a person
 * @author anuj
 * @date Jan 18, 2014
 *
 */
@NodeEntity
public class Complaint extends BaseNode {

	@Indexed(indexType=IndexType.FULLTEXT)
	private String title;
	private String description;
	@RelatedTo(type="IS_AT")
	private GeoPoint geoPoint;
	@RelatedTo(type="BELONGS_TO")
	private Category category;
	@RelatedToVia(type="LODGED_BY")
	private Person person;
	@RelatedToVia(type="SERVED_BY")
	private ExecutiveBodyAdmin executiveBodyAdmin;
	private String status;
	@RelatedTo(type="ENDORSED_BY", elementClass=Person.class)
	private Set<Person> endorsements;
	@RelatedTo(type="SERVED_BY")
	private Set<PoliticalBodyAdmin> servants;
	private Set<Photo> photos;
	private Set<Video> videos;
	
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
	public GeoPoint getGeoPoint() {
		return geoPoint;
	}
	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public ExecutiveBodyAdmin getExecutiveBodyAdmin() {
		return executiveBodyAdmin;
	}
	public void setExecutiveBodyAdmin(ExecutiveBodyAdmin executiveBodyAdmin) {
		this.executiveBodyAdmin = executiveBodyAdmin;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Set<Person> getEndorsements() {
		return endorsements;
	}
	public void setEndorsements(Set<Person> endorsements) {
		this.endorsements = endorsements;
	}
	public Set<PoliticalBodyAdmin> getServants() {
		return servants;
	}
	public void setServants(Set<PoliticalBodyAdmin> servants) {
		this.servants = servants;
	}
	public Set<Photo> getPhotos() {
		return photos;
	}
	public void setPhotos(Set<Photo> photos) {
		this.photos = photos;
	}
	public Set<Video> getVideos() {
		return videos;
	}
	public void setVideos(Set<Video> videos) {
		this.videos = videos;
	}
	
	
	
}
