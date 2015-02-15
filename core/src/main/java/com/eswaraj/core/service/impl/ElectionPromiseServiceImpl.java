package com.eswaraj.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.ElectionPromiseService;
import com.eswaraj.domain.nodes.ElectionManifestoPromise;
import com.eswaraj.domain.repo.ElectionManifestoPromiseRepository;
import com.google.gson.Gson;

@Service
@Transactional
public class ElectionPromiseServiceImpl extends BaseService implements ElectionPromiseService {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ElectionManifestoPromiseRepository electionManifestoPromiseRepository;

    private Gson gson = new Gson();

    @Override
    public String getElectionPromisesByPoliticalAdminId(Long politicalAdminId) throws ApplicationException {
        logger.info("getting Election promises for leader {}", politicalAdminId);
        List<ElectionManifestoPromise> electionManifestoPromises = electionManifestoPromiseRepository.getAllPromisesOfPoliticalAdmin(politicalAdminId);
        return gson.toJson(electionManifestoPromises);
    }

}
