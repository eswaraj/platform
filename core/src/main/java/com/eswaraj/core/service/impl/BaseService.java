package com.eswaraj.core.service.impl;

import java.io.Serializable;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.core.exceptions.ApplicationException;

public abstract class BaseService implements Serializable {

	protected <DbType> DbType getObjectIfExistsElseThrowExcetpion(Long id, String objectName, GraphRepository<DbType> repository) throws ApplicationException{
		DbType dbObject = null;
		if(id != null && id > 0){
			dbObject = repository.findOne(id);
		}
		if(dbObject == null){
			throw new ApplicationException("No such " + objectName + " exists[id="+ id +"]");
		}
		return dbObject;
	}


}
