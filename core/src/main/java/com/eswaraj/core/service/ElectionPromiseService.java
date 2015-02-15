package com.eswaraj.core.service;

import com.eswaraj.core.exceptions.ApplicationException;

public interface ElectionPromiseService {

    String getElectionPromisesByPoliticalAdminId(Long politicalAdminId) throws ApplicationException;
}
