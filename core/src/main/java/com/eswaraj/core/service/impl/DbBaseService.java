package com.eswaraj.core.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.core.exceptions.ApplicationException;

public class DbBaseService implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected <DbType> DbType getObjectIfExistsElseThrowExcetpion(Long id, String objectName, GraphRepository<DbType> repository) throws ApplicationException {
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

    public <DbType> List<DbType> convertToList(EndResult<DbType> dbTypeCollection) throws ApplicationException {
        if (dbTypeCollection == null) {
            return new ArrayList<DbType>();
        }
        List<DbType> webTypeList = new ArrayList<>();
        for (DbType oneDbType : dbTypeCollection) {
            webTypeList.add(oneDbType);
        }
        return webTypeList;
    }

}
