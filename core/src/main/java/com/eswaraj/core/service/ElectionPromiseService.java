package com.eswaraj.core.service;

import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.v1.ElectionManifestoPromiseDto;

public interface ElectionPromiseService {

    List<ElectionManifestoPromiseDto> getElectionPromisesByPoliticalAdminId(Long politicalAdminId) throws ApplicationException;
}
