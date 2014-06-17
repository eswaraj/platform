package com.eswaraj.core.convertors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.BaseDto;

public abstract class BaseConvertor<DbType, WebType> {


	protected DbType getObjectIfExists(BaseDto dto, String objectName, GraphRepository<DbType> repository) throws ApplicationException{
		DbType dbObject = null;
		if(dto.getId() == null || dto.getId() <= 0){
			dto.setId(null);
			return dbObject;
		}else{
			dbObject = repository.findOne(dto.getId());
			if(dbObject == null){
				throw new ApplicationException("No such " + objectName + " exists[id="+ dto.getId() +"]");
			}
		}
		
		return dbObject;
	}
	protected <AnyType> AnyType getObjectIfExists(Long nodeId, String objectName, GraphRepository<AnyType> repository) throws ApplicationException{
		AnyType dbObject = null;
		if(nodeId == null || nodeId <= 0){
			return dbObject;
		}else{
			dbObject = repository.findOne(nodeId);
			if(dbObject == null){
				throw new ApplicationException("No such " + objectName + " exists[id="+ nodeId +"]");
			}
		}
		
		return dbObject;
	}
	public DbType convert(WebType webDto) throws ApplicationException{
		return convertInternal(webDto);
	}
	
	protected abstract DbType convertInternal(WebType webDto) throws ApplicationException;
	
	public WebType convertBean(DbType dbDto) throws ApplicationException{
		if(dbDto == null){
			return null;
		}
		return convertBeanInternal(dbDto);
	}
	
	protected abstract WebType convertBeanInternal(DbType dbDto) throws ApplicationException;
	
	public List<WebType> convertBeanList(Collection<DbType> dbTypeCollection) throws ApplicationException{
		if(dbTypeCollection == null){
			return new ArrayList<WebType>();
		}
		List<WebType> webTypeList = new ArrayList<>();
		for(DbType oneDbType:dbTypeCollection){
			webTypeList.add(convertBean(oneDbType));
		}
		return webTypeList;
	}
	
	public List<WebType> convertBeanList(EndResult<DbType> dbTypeCollection) throws ApplicationException{
		if(dbTypeCollection == null){
			return new ArrayList<WebType>();
		}
		List<WebType> webTypeList = new ArrayList<>();
		for(DbType oneDbType:dbTypeCollection){
			webTypeList.add(convertBean(oneDbType));
		}
		return webTypeList;
	}
}
