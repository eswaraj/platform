package com.eswaraj.core.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.commons.io.IOUtils;
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

    protected String readFile(String file) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream(file);
        String json = IOUtils.toString(inputStream, "UTF-8");
        return json;
    }


}
