package com.eswaraj.domain.repo;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Category;

public interface CategoryRepository extends GraphRepository<Category>{

	public Category getById(Long id);
	
	@Query("START n=node(*) WHERE HAS(n.root) AND n.__type__ = 'com.eswaraj.domain.nodes.Category' and n.root = true RETURN n")
	public Collection<Category> getAllRootCategories();
	
	@Query("start category=node({0}) match (category)<-[:BELONGS_TO]-(childCategory) return childCategory")
    public Collection<Category> findAllChildCategoryOfParentCategory(Category category);

}
