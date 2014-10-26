package com.eswaraj.cache;

import java.util.Collection;
import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;

public interface PersonCache {

    /**
     * Refresh a person in cache
     * 
     * @param personId
     * @throws ApplicationException
     */
    void refreshPerson(long personId) throws ApplicationException;

    /**
     * return a person by ID
     * 
     * @param personId
     * @return person Json as String
     * @throws ApplicationException
     */
    String getPersonById(long personId) throws ApplicationException;

    /**
     * return all persons by given IDs
     * 
     * @param List of personIds
     * @return List of all persons as Json String
     * @throws ApplicationException
     */
    List<String> getPersonsByIds(Collection<Long> personIds) throws ApplicationException;
}
