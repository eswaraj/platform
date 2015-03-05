package com.eswaraj.core.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.ElectionPromiseService;
import com.eswaraj.domain.nodes.ElectionManifestoPromise;
import com.eswaraj.domain.repo.ElectionManifestoPromiseRepository;
import com.eswaraj.web.dto.v1.ElectionManifestoPromiseDto;

@Service
@Transactional
public class ElectionPromiseServiceImpl extends BaseService implements ElectionPromiseService {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ElectionManifestoPromiseRepository electionManifestoPromiseRepository;

    @Override
    public List<ElectionManifestoPromiseDto> getElectionPromisesByPoliticalAdminId(Long politicalAdminId) throws ApplicationException {
        logger.info("getting Election promises for leader {}", politicalAdminId);
        List<ElectionManifestoPromise> electionManifestoPromises = electionManifestoPromiseRepository.getAllPromisesOfPoliticalAdmin(politicalAdminId);
        if(electionManifestoPromises == null || electionManifestoPromises.isEmpty()){
            return Collections.emptyList();
        }
        List<ElectionManifestoPromiseDto> returnDtos = new ArrayList<ElectionManifestoPromiseDto>(electionManifestoPromises.size());
        for (ElectionManifestoPromise oneElectionManifestoPromise : electionManifestoPromises) {
            ElectionManifestoPromiseDto oneElectionManifestoPromiseDto = new ElectionManifestoPromiseDto();
            oneElectionManifestoPromiseDto.setDescription(oneElectionManifestoPromise.getDescription());
            oneElectionManifestoPromiseDto.setId(oneElectionManifestoPromise.getId());
            oneElectionManifestoPromiseDto.setTitle(oneElectionManifestoPromise.getTitle());
            oneElectionManifestoPromiseDto.setStatus(oneElectionManifestoPromise.getStatus());
            oneElectionManifestoPromiseDto.setDeliveryTime(oneElectionManifestoPromise.getDeliveryTime());
            returnDtos.add(oneElectionManifestoPromiseDto);
        }
        return returnDtos;
    }

}
