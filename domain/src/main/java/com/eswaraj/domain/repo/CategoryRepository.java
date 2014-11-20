package com.eswaraj.domain.repo;

import java.util.Collection;
import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Complaint;

public interface CategoryRepository extends GraphRepository<Category>{

	public Category getById(Long id);
	
    @Query("START n=node(*) WHERE HAS(n.root) AND (n.__type__ = 'com.eswaraj.domain.nodes.Category' or n.__type__ = 'Category') and n.root = true RETURN n")
    public List<Category> getAllRootCategories();
	
    @Query("start category=node({0}) match (category)<-[:BELONGS_TO]-(childCategory) where childCategory.__type__ = 'com.eswaraj.domain.nodes.Category' return childCategory")
    public Collection<Category> findAllChildCategoryOfParentCategory(Category category);

    @Query("start category=node({0}) match (category)<-[:BELONGS_TO]-(childCategory) where (childCategory.__type__ = 'com.eswaraj.domain.nodes.Category' or childCategory.__type__ = 'Category') return childCategory")
    public List<Category> findAllChildCategoryOfParentCategory(Long categoryId);

    @Query("start childCategory=node({0}) match (category)<-[:BELONGS_TO]-(childCategory) where category.__type__ = 'com.eswaraj.domain.nodes.Category' return category")
    public Category getParentCategory(Category category);

    @Query("start complaint=node({0}) match (complaint)-[:BELONGS_TO]->(category) where category.__type__ = 'com.eswaraj.domain.nodes.Category' return category")
    public Collection<Category> getCategoriesOfComplaints(Complaint complaint);
}
