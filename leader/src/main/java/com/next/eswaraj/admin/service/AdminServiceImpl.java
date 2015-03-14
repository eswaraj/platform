package com.next.eswaraj.admin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Comment;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.FacebookAccount;
import com.eswaraj.domain.nodes.FacebookApp;
import com.eswaraj.domain.nodes.LeaderTempFacebookAccount;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.Photo;
import com.eswaraj.domain.nodes.PoliticalAdminComplaintStatus;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.PoliticalBodyAdminStaff;
import com.eswaraj.domain.nodes.User;
import com.eswaraj.domain.nodes.extended.ComplaintSearchResult;
import com.eswaraj.domain.nodes.extended.PoliticalBodyAdminStaffSearchResult;
import com.eswaraj.domain.nodes.relationships.ComplaintComment;
import com.eswaraj.domain.nodes.relationships.ComplaintPoliticalAdmin;
import com.eswaraj.domain.nodes.relationships.FacebookAppPermission;
import com.eswaraj.domain.repo.CategoryRepository;
import com.eswaraj.domain.repo.CommentRepository;
import com.eswaraj.domain.repo.ComplaintCommentRepository;
import com.eswaraj.domain.repo.ComplaintPoliticalAdminRepository;
import com.eswaraj.domain.repo.ComplaintRepository;
import com.eswaraj.domain.repo.FacebookAccountRepository;
import com.eswaraj.domain.repo.FacebookAppPermissionRepository;
import com.eswaraj.domain.repo.FacebookAppRepository;
import com.eswaraj.domain.repo.LeaderTempFacebookAccountRepository;
import com.eswaraj.domain.repo.PersonRepository;
import com.eswaraj.domain.repo.PhotoRepository;
import com.eswaraj.domain.repo.PoliticalBodyAdminRepository;
import com.eswaraj.domain.repo.PoliticalBodyAdminStaffRepository;
import com.eswaraj.domain.repo.UserRepository;

@Service
public class AdminServiceImpl implements AdminService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${eswaraj_facebook_app_id}")
    private String facebokAppId;

    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private PoliticalBodyAdminRepository politicalBodyAdminRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ComplaintPoliticalAdminRepository complaintPoliticalAdminRepository;
    @Autowired
    private ComplaintCommentRepository complaintCommentRepository;
    @Autowired
    private PoliticalBodyAdminStaffRepository politicalBodyAdminStaffRepository;
    @Autowired
    private LeaderTempFacebookAccountRepository leaderTempFacebookAccountRepository;
    @Autowired
    private FacebookAccountRepository facebookAccountRepository;
    @Autowired
    private FacebookAppRepository facebookAppRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FacebookAppPermissionRepository facebookAppPermissionRepository;

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

    @Override
    public List<Category> getAllcategories() throws ApplicationException {
        return categoryRepository.getAllCategories();
    }

    @Override
    public Complaint saveComplaint(Complaint complaint) throws ApplicationException {
        return complaintRepository.save(complaint);
    }

    @Override
    public List<Comment> getComplaintComments(Long complaintId) throws ApplicationException {
        return commentRepository.findAllCommentsByComplaintId(complaintId);
    }

    @Override
    public ComplaintPoliticalAdmin saveComplaintPoliticalAdmin(ComplaintPoliticalAdmin complaintPoliticalAdmin) throws ApplicationException {
        return complaintPoliticalAdminRepository.save(complaintPoliticalAdmin);
    }

    @Override
    public Comment saveComplaintComment(Complaint complaint, PoliticalBodyAdmin politicalBodyAdmin, Long personId, String text) throws ApplicationException {
        Person createdBy = personRepository.findOne(personId);
        Comment comment = new Comment();
        comment.setCreatedBy(createdBy);
        comment.setCreationTime(new Date());
        comment.setPoliticalBodyAdmin(politicalBodyAdmin);
        comment.setText(text);
        comment = commentRepository.save(comment);
        System.out.println("Save Comment = " + comment);

        ComplaintComment complaintComment = new ComplaintComment();
        complaintComment.setComment(comment);
        complaintComment.setComplaint(complaint);
        complaintComment.setDateCreated(new Date());
        complaintComment.setDateModified(new Date());
        complaintComment = complaintCommentRepository.save(complaintComment);
        return comment;
    }

    @Override
    public void markComplaintViewed(Long complaintId, Long politicalBodyAdminId) throws ApplicationException {
        ComplaintPoliticalAdmin complaintPoliticalAdmin = complaintPoliticalAdminRepository.getComplaintPoliticalAdminRelation(complaintId, politicalBodyAdminId);
        if (!complaintPoliticalAdmin.isViewed()) {
            complaintPoliticalAdmin.setViewDate(new Date());
            complaintPoliticalAdmin.setViewed(true);
            complaintPoliticalAdmin.setStatus(PoliticalAdminComplaintStatus.Viewed);
            complaintPoliticalAdmin = complaintPoliticalAdminRepository.save(complaintPoliticalAdmin);
        }
    }

    @Override
    public List<PoliticalBodyAdminStaffSearchResult> getAdminStaffList(Long politicalBodyAdminId) throws ApplicationException {
        List<PoliticalBodyAdminStaffSearchResult> staff = politicalBodyAdminStaffRepository.searchPoliticalAdminStaffForAdmin(politicalBodyAdminId);
        return staff;
    }

    @Override
    public List<Person> searchPersonByName(String name) throws ApplicationException {
        logger.info("Searching person by Name : {}", name);
        if (StringUtils.isEmpty(name)) {
            return new ArrayList<Person>();
        }
        return personRepository.searchPersonByName("name:*" + name + "*");
    }

    @Override
    public PoliticalBodyAdminStaff savePoliticalBodyAdminStaff(PoliticalBodyAdmin politicalBodyAdmin, Person person, String post) throws ApplicationException {

        PoliticalBodyAdminStaff dbPoliticalBodyAdminStaff = politicalBodyAdminStaffRepository.getStaffByPoliticalAdminAndPerson(politicalBodyAdmin, person);

        if (dbPoliticalBodyAdminStaff == null) {
            dbPoliticalBodyAdminStaff = new PoliticalBodyAdminStaff();
            dbPoliticalBodyAdminStaff.setPoliticalBodyAdmin(politicalBodyAdmin);
            dbPoliticalBodyAdminStaff.setPerson(person);
        }

        dbPoliticalBodyAdminStaff.setPost(post);

        dbPoliticalBodyAdminStaff = politicalBodyAdminStaffRepository.save(dbPoliticalBodyAdminStaff);
        return dbPoliticalBodyAdminStaff;
    }

    @Override
    public PoliticalBodyAdminStaff removePoliticalBodyAdminStaff(PoliticalBodyAdminStaff politicalBodyAdminStaff) throws ApplicationException {
        politicalBodyAdminStaffRepository.delete(politicalBodyAdminStaff.getId());
        return politicalBodyAdminStaff;
    }

    @Override
    public LeaderTempFacebookAccount getFacebookAccountRequestForPerson(Person person) throws ApplicationException {
        return leaderTempFacebookAccountRepository.getLeaderTempFacebookAccountByPerson(person);
    }

    @Override
    public FacebookAccount getFacebookAccountByPerson(Person person) throws ApplicationException {
        return facebookAccountRepository.getFacebookAccountByPerson(person);
    }

    @Override
    public void validateJoinRequest(String personId, String requestId, String emailId) throws ApplicationException {
        Person person = new Person();
        person.setId(Long.parseLong(personId));
        LeaderTempFacebookAccount leaderTempFacebookAccount = leaderTempFacebookAccountRepository.getLeaderTempFacebookAccountByPerson(person);
        if (leaderTempFacebookAccount == null) {
            throw new ApplicationException("Invalid Request, error code 101");
        }
        if (!leaderTempFacebookAccount.getRequestId().equals(requestId)) {
            throw new ApplicationException("Invalid Request, error code 102");
        }
        if (!leaderTempFacebookAccount.getEmail().equalsIgnoreCase(emailId)) {
            throw new ApplicationException("Invalid Request, error code 103");
        }

        // Check if facebook account already exists for given Email id
        logger.info("Getting Facebook account by email : {}", emailId);
        FacebookAccount facebookAccount = facebookAccountRepository.getFacebookAccountByEmail(emailId);
        logger.info("facebookAccount by email : {} is {}", emailId, facebookAccount);
        if (facebookAccount != null) {
            throw new ApplicationException("Invalid Request, error code 104");
        }
        facebookAccount = facebookAccountRepository.getFacebookAccountByPerson(person);
        if (facebookAccount != null) {
            throw new ApplicationException("Invalid Request, error code 105");
        }

    }

    @Override
    public void linkLeaderToFacebookAccount(String personId, String requestId, String emailId, ConnectionData facebookConnectionData) throws ApplicationException {
        logger.info("Validating Request");
        validateJoinRequest(personId, requestId, emailId);
        
        
        Facebook facebook = new FacebookTemplate(facebookConnectionData.getAccessToken());
        FacebookProfile facebookUserProfile = facebook.userOperations().getUserProfile();
        String facebookUserId = facebookUserProfile.getId();
        logger.info("Getting Facebook Account for Id : {}", facebookUserId);

        FacebookAccount facebookAccount = facebookAccountRepository.getFacebookAccountByFacebookUserId(facebookUserId);
        if (facebookAccount != null) {
            throw new ApplicationException("Invalid Request : error code 106");
        }
        Person person = personRepository.findOne(Long.parseLong(personId));
        if(person == null){
            throw new ApplicationException("Invalid Request : error code 107");
        }

        logger.info("getOrCreateFacebookApp : {}", facebokAppId);
        FacebookApp facebookApp = getOrCreateFacebookApp(facebokAppId);
        logger.info("create new user for Person : {}", person);
        User user = new User();
        user.setPerson(person);
        user = userRepository.save(user);
        logger.info("User Created : {}", user);
        // Create a new new afcebook account and attach it to user
        facebookAccount = new FacebookAccount();
        facebookAccount.setDateCreated(new Date());
        facebookAccount.setDateModified(new Date());
        facebookAccount.setEmail(facebookUserProfile.getEmail());
        facebookAccount.setGender(facebookUserProfile.getGender());
        facebookAccount.setUserName(facebookUserProfile.getUsername());
        facebookAccount.setUser(user);
        facebookAccount.setName(facebookUserProfile.getName());
        facebookAccount.setFacebookUserId(facebookUserProfile.getId());
        facebookAccount.setExternalId(UUID.randomUUID().toString());
        facebookAccount = facebookAccountRepository.save(facebookAccount);
        logger.info("facebookAccount Created : {}", facebookAccount);

        FacebookAppPermission facebookAppPermission = new FacebookAppPermission();
        facebookAppPermission.setFacebookAccount(facebookAccount);
        facebookAppPermission.setFacebookApp(facebookApp);
        if (facebookConnectionData.getExpireTime() != null) {
            facebookAppPermission.setExpireTime(new Date(facebookConnectionData.getExpireTime()));
        }
        facebookAppPermission.setToken(facebookConnectionData.getAccessToken());
        facebookAppPermission.setLastLoginTime(new Date());

        facebookAppPermission = facebookAppPermissionRepository.save(facebookAppPermission);
        logger.info("facebookAppPermission Created : {}", facebookAppPermission);

            // Update Person Info too
    }

    protected FacebookApp getOrCreateFacebookApp(String facebookAppId) {
        FacebookApp facebookApp = facebookAppRepository.findByPropertyValue("appId", facebookAppId);
        if (facebookApp == null) {
            facebookApp = new FacebookApp();
            facebookApp.setAppId(facebookAppId);
            facebookApp = facebookAppRepository.save(facebookApp);
        }
        return facebookApp;
    }

}
