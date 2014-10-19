package com.eswaraj.api.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.ComplaintService;
import com.eswaraj.core.service.FileService;
import com.eswaraj.web.dto.ComplaintDto;
import com.eswaraj.web.dto.ComplaintStatusChangeByPoliticalAdminRequestDto;
import com.eswaraj.web.dto.ComplaintViewdByPoliticalAdminRequestDto;
import com.eswaraj.web.dto.PhotoDto;
import com.eswaraj.web.dto.PoliticalAdminComplaintDto;
import com.eswaraj.web.dto.SaveComplaintRequestDto;
import com.eswaraj.web.dto.comment.CommentSaveRequestDto;
import com.eswaraj.web.dto.comment.CommentSaveResponseDto;
import com.google.gson.Gson;

/**
 * 
 * @author anuj
 * @data Jun 22, 2014
 */

@Controller
public class ComplaintController extends BaseController{
	
	@Autowired
	private ComplaintService complaintService;	
	@Autowired
	private FileService fileService;
	@Value("${aws_s3_directory_for_complaint_photo}") 
	private String awsDirectoryForComplaintPhoto;

    @RequestMapping(value = "/api/v0/user/complaints/{userId}", method = RequestMethod.GET)
    public @ResponseBody List<ComplaintDto> getUserComplaints(@PathVariable Long userId, @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "count", required = false) Integer end) throws ApplicationException {
		if(start == null){
			return complaintService.getAllUserComplaints(userId);	
		}else{
			return complaintService.getPagedUserComplaints(userId, start, end);	
		}
		
	}

    @RequestMapping(value = "/api/v0/device/complaints/{userId}", method = RequestMethod.GET)
    public @ResponseBody List<ComplaintDto> getDeviceComplaints(@PathVariable String deviceId, @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "count", required = false) Integer count) throws ApplicationException {
		if(start == null){
			return complaintService.getAllUserComplaints(deviceId);	
		}else{
			return complaintService.getPagedDeviceComplaints(deviceId, start, count);	
		}
		
	}
	
    @RequestMapping(value = "/api/v0/complaint", method = RequestMethod.POST)
	public @ResponseBody ComplaintDto saveComplaint(HttpServletRequest httpServletRequest) throws ApplicationException, IOException, ServletException {
		
		printInfo(httpServletRequest);
		Part saveComplaintRequestPart = httpServletRequest.getPart("SaveComplaintRequest");
		StringWriter writer = new StringWriter();
		IOUtils.copy(saveComplaintRequestPart.getInputStream(), writer);
		String saveComplaintRequestString = writer.toString();
		
		SaveComplaintRequestDto saveComplaintRequestDto = new Gson().fromJson(saveComplaintRequestString, SaveComplaintRequestDto.class);
        updateRandomDelhiPoint(saveComplaintRequestDto);
		ComplaintDto savedComplaintDto = complaintService.saveComplaint(saveComplaintRequestDto);
        logger.info("Complaint Saved : {}", savedComplaintDto);
		addPhoto(httpServletRequest, savedComplaintDto);
		
		return savedComplaintDto;
	}

    @RequestMapping(value = "/api/v0/complaint/politicaladmin/{politicalAdminId}", method = RequestMethod.GET)
    public @ResponseBody List<PoliticalAdminComplaintDto> getComplaintsOfPoliticalAdmin(HttpServletRequest httpServletRequest, @PathVariable Long politicalAdminId) throws ApplicationException, IOException, ServletException {
        long start = getLongParameter(httpServletRequest, "start", 0);
        long pageSize = getLongParameter(httpServletRequest, "count", 10);

        List<PoliticalAdminComplaintDto> politicalAdminComplaints = complaintService.getAllComplaintsOfPoliticalAdmin(politicalAdminId, start, pageSize);
        return politicalAdminComplaints;
    }

    @RequestMapping(value = "/api/v0/complaint/politicaladmin/view", method = RequestMethod.POST)
    public @ResponseBody PoliticalAdminComplaintDto updateComplaintViewStatus(HttpServletRequest httpServletRequest,
            @RequestBody ComplaintViewdByPoliticalAdminRequestDto complaintViewdByPoliticalAdminRequestDto) throws ApplicationException, IOException, ServletException {
        return complaintService.updateComplaintViewStatus(complaintViewdByPoliticalAdminRequestDto);
    }

    @RequestMapping(value = "/api/v0/complaint/politicaladmin/status", method = RequestMethod.POST)
    public @ResponseBody PoliticalAdminComplaintDto getComplaintsOfPoliticalAdmin(HttpServletRequest httpServletRequest,
            @RequestBody ComplaintStatusChangeByPoliticalAdminRequestDto complaintStatusChangeByPoliticalAdminRequestDto) throws ApplicationException,
            IOException, ServletException {
        return complaintService.updateComplaintPoliticalAdminStatus(complaintStatusChangeByPoliticalAdminRequestDto);
    }

    @RequestMapping(value = "/api/v0/complaint/politicaladmin/comment", method = RequestMethod.POST)
    public @ResponseBody CommentSaveResponseDto postComment(HttpServletRequest httpServletRequest, @RequestBody CommentSaveRequestDto commentRequestDto)
            throws ApplicationException, IOException, ServletException {
        return complaintService.commentOnComplaint(commentRequestDto);
    }

    private void updateRandomDelhiPoint(SaveComplaintRequestDto saveComplaintRequestDto) {
        Double[][] delhiPoints = { { 77.04124994150143, 28.623132677360626 }, { 77.03290098437007, 28.62572366266242 }, { 77.02688134045275, 28.631229145737702 },
                { 77.04084506670374, 28.62302782260635 }, { 77.0316069082104, 28.623910892538678 }, { 77.03226119037603, 28.622169846160872 }, { 77.03833192102502, 28.627098104023915 },
                { 77.02911718817295, 28.62723331624312 }, { 77.03134915723099, 28.628478560676783 }, { 77.02709769991337, 28.623584337323965 }, { 77.05134874040998, 28.622902869899004 },
                { 77.05093245708098, 28.62640963840088 }, { 77.05209790776668, 28.624067808180904 }, { 77.05365145431658, 28.623311535164476 }, { 77.05289119784607, 28.622288763256815 },
                { 77.05017797702818, 28.62309387024103 }, { 77.05258743664093, 28.622988160167722 }, { 77.05018022789844, 28.624822518189937 }, { 77.05232495293377, 28.621811041657583 },
                { 77.05275936194286, 28.62117227023664 }, { 77.29573923960037, 28.601486770656074 }, { 77.30248061496185, 28.588003559449014 }, { 77.31187481021168, 28.596051380035913 },
                { 77.30445887594657, 28.594215068007408 }, { 77.30983478980893, 28.599438341822573 }, { 77.29854539576917, 28.598142104761877 }, { 77.31013442382826, 28.598518620798316 },
                { 77.30346567552043, 28.591739817898574 }, { 77.30580147806417, 28.588337047928725 }, { 77.3009287884298, 28.591624705713695 }, { 77.27065589271095, 28.71697945710198 },
                { 77.27695090421973, 28.715164485501678 }, { 77.27199069263197, 28.718372588940905 }, { 77.2778597994308, 28.719737499801997 }, { 77.27476261907766, 28.717761217463273 },
                { 77.27348727785417, 28.716740963826787 }, { 77.26725985301917, 28.717533281566986 }, { 77.26990065594367, 28.72146671820412 }, { 77.27028381308709, 28.717819407599155 },
                { 77.27292767678696, 28.727410057606196 }, { 77.05886382300942, 28.6101120629536 }, { 77.06225801015344, 28.609914758801608 }, { 77.06471555008677, 28.606725696574266 },
                { 77.05283152462609, 28.613843225209937 }, { 77.05757277114077, 28.609650184722444 }, { 77.05677083156584, 28.606565116597384 }, { 77.06253679749426, 28.607793220987652 },
                { 77.05085380949298, 28.60987202014995 }, { 77.05616230189459, 28.611072923286162 }, { 77.04948473908807, 28.61104087859465 }, { 77.2794784220705, 28.69071964440402 },
                { 77.2882605297475, 28.687372688740968 }, { 77.27914043377417, 28.691368357577453 }, { 77.28115402013795, 28.692047674766158 }, { 77.29194295042338, 28.68656849584905 },
                { 77.27951089671889, 28.688539061070237 }, { 77.28950097169962, 28.68509777615662 }, { 77.28334715292121, 28.688247978026787 }, { 77.29163254615051, 28.683324429722816 },
                { 77.28600450239458, 28.68743226905715 }, { 77.06023045541676, 28.70261418200059 }, { 77.04875899387807, 28.712180511543664 }, { 77.05813148123102, 28.704800860499127 },
                { 77.06052767203701, 28.702425556309258 }, { 77.06230396758531, 28.703763496905815 }, { 77.05669166075485, 28.70639785122411 }, { 77.05200778561161, 28.712048262596873 },
                { 77.06250710051522, 28.702982799257978 }, { 77.06214420857195, 28.705445393894767 }, { 77.05676727401324, 28.704073728044936 }, { 77.33026392824999, 28.616424142497795 },
                { 77.32778737430692, 28.614445052671456 }, { 77.32694523551481, 28.618114862880294 }, { 77.33875592316225, 28.60994185285954 }, { 77.33468774504922, 28.604630514316824 },
                { 77.32664320305693, 28.61566524956902 }, { 77.32232821879539, 28.613546432126906 }, { 77.33005515752022, 28.613933425011318 }, { 77.3307280197361, 28.611968663219976 },
                { 77.32968936615441, 28.614741215262395 } };
        Random random = new Random(System.currentTimeMillis());
        int nextNumber = random.nextInt(delhiPoints.length);
        saveComplaintRequestDto.setLattitude(delhiPoints[nextNumber][1]);
        saveComplaintRequestDto.setLongitude(delhiPoints[nextNumber][0]);
    }
	private void addPhoto(HttpServletRequest httpServletRequest, ComplaintDto complaintDto) throws IOException, ServletException, ApplicationException{
		String imageHttpUrl = "";
		Part uploadedImagePart = httpServletRequest.getPart("img");
		if(uploadedImagePart != null){
			String directory = awsDirectoryForComplaintPhoto+"/" + complaintDto.getId();
			String fileName = getFileName(uploadedImagePart.getSubmittedFileName());
			imageHttpUrl = fileService.saveFile(directory, fileName, uploadedImagePart.getInputStream());
			PhotoDto photoDto = new PhotoDto();
			photoDto.setOrgUrl(imageHttpUrl);
            try {
                complaintService.addPhotoToComplaint(complaintDto.getId(), photoDto);
                logger.info("Photo added succesfully");
                logger.info("Photos : " + complaintService.getComplaintPhotos(complaintDto.getId()));

            } catch (Exception ex) {
                logger.error("Unable to attach Photo", ex);
            }

		}
	}
	private void printInfo(HttpServletRequest httpServletRequest){
		Enumeration<String> params = httpServletRequest.getParameterNames();
		System.out.println("*** ALL PARAMETERS*****");
		while(params.hasMoreElements()){
			System.out.println(params.nextElement());
		}
		{
			Enumeration<String> headers = httpServletRequest.getHeaderNames();
			System.out.println("*** ALL HEADERS*****");
			while(headers.hasMoreElements()){
				System.out.println(headers.nextElement());
			}
			
		}
		try{
			Collection<Part> parts = httpServletRequest.getParts();
			System.out.println("*** ALL Parts*****");
			for(Part onePart : parts){
				System.out.println(onePart.getName() +" , "+onePart.getSize() +" , "+ onePart.getContentType() +" , "+onePart.getSubmittedFileName() +" , "+onePart.getInputStream());
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	@RequestMapping(value = "/mobile/complaints", method = RequestMethod.POST)
	public @ResponseBody String saveComplaintsasa(HttpServletRequest httpServletRequest) throws ApplicationException {
		
		printInfo(httpServletRequest);
		return "Done";
	}
	/*
	@RequestMapping(value = "/user/complaints/{userId}", method = RequestMethod.GET)
	public @ResponseBody List<ComplaintDto> getUserComplaints(@PathVariable Long userId) throws ApplicationException {
		
	}
	*/
}
