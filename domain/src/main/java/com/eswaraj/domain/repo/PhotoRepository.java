package com.eswaraj.domain.repo;

import java.util.Collection;
import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Photo;

public interface PhotoRepository extends GraphRepository<Photo>{
	
    @Query("start complaint=node({0}) match (complaint)-[:PHOTOS_OF_COMPLAINT]->(photo) where photo.__type__ = 'com.eswaraj.domain.nodes.Photo' return photo")
    public Collection<Photo> getComplaintPhotos(Complaint complaint);

    @Query("start complaint=node({0}) match (complaint)-[:PHOTOS_OF_COMPLAINT]->(photo) where photo.__type__ = 'com.eswaraj.domain.nodes.Photo' return photo")
    public List<Photo> getComplaintPhotos(Long complaintId);

}
