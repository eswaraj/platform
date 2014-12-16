package com.next.eswaraj.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.stereotype.Service;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.Photo;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.extended.ComplaintSearchResult;
import com.eswaraj.domain.repo.ComplaintRepository;
import com.eswaraj.domain.repo.PersonRepository;
import com.eswaraj.domain.repo.PhotoRepository;
import com.eswaraj.domain.repo.PoliticalBodyAdminRepository;

@Service
public class AdminServiceImpl implements AdminService {


    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private PoliticalBodyAdminRepository politicalBodyAdminRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private PersonRepository personRepository;

    @Override
    public List<Complaint> getPoliticalAdminComplaints(Long politicalAdminId) throws ApplicationException {
        return null;
    }

    @Override
    public List<ComplaintSearchResult> getPoliticalAdminComplaintsAll(Long politicalAdminId) throws ApplicationException {
        return complaintRepository.searchAllPagedComplaintsOfPoliticalAdmin(politicalAdminId, 0, 1000);
    }

    private <T> List<T> convertToList(EndResult<T> dbResult) {
        List<T> returnList = new ArrayList<>();
        for (T oneT : dbResult) {
            returnList.add(oneT);
        }
        return returnList;
    }

    @Override
    public List<PoliticalBodyAdmin> getUserPoliticalBodyAdmins(Long userId) throws ApplicationException {
        return politicalBodyAdminRepository.getActivePoliticalAdminHistoryByUserId(userId);
    }

    @Override
    public List<Photo> getComplaintPhotos(Long complaintId) throws ApplicationException {
        return photoRepository.getComplaintPhotos(complaintId);
    }

    @Override
    public List<Person> getComplaintCreators(Long complaintId) throws ApplicationException {
        return personRepository.getPersonsLoggedComplaint(complaintId);
    }

}
