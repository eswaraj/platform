package com.eswaraj.core.service;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Person;
import com.google.gson.JsonArray;

public interface TempService {
    /**
     * Temp Service to import Persons
     * 
     * @param person
     * @return
     * @throws ApplicationException
     */
    @Deprecated
    Person savePerson(Person person) throws ApplicationException;

    @Deprecated
    JsonArray createLocationAndMpRecord(String body) throws ApplicationException;

    @Deprecated
    JsonArray createLocationAndWardRecord(String body) throws ApplicationException;

}
