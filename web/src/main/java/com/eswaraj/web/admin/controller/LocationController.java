package com.eswaraj.web.admin.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.CustomService;
import com.eswaraj.core.service.FileService;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.web.dto.LocationDto;
import com.eswaraj.web.dto.LocationTypeDto;
import com.eswaraj.web.dto.LocationTypeJsonDto;

@Controller
public class LocationController extends BaseController{

	@Autowired
	private LocationService locationService;
	
	@Autowired
	private CustomService customService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private RedisTemplate<String, List<Long>> redisTemplate;
	
	@RequestMapping(value = "/locations", method = RequestMethod.GET)
	public ModelAndView showIndexsPages(ModelAndView mv) {
		mv.setViewName("locationManager");
		return mv;
	}
	@RequestMapping(value="/location/upload/{locationId}", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("name") String name,
            @RequestParam("file") MultipartFile file, @PathVariable Long locationId){
        if (!file.isEmpty()) {
            try {
            	customService.processLocationBoundaryFile(locationId, file.getInputStream());
                return "You successfully uploaded " + name + " into " + name + "-uploaded !";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }
	
	@RequestMapping(value = "/ajax/locationtype/get", method = RequestMethod.GET)
	public @ResponseBody LocationTypeJsonDto getLocationTypes(ModelAndView mv) throws ApplicationException {
		LocationTypeJsonDto locationTypeJsonDto = locationService.getLocationTypes("beingIgnored");
		return locationTypeJsonDto;
	}
	@RequestMapping(value = "/ajax/locationtype/getchild/{parentLocationTypeId}", method = RequestMethod.GET)
	public @ResponseBody List<LocationTypeDto> getLocationTypeChildren(ModelAndView mv, @PathVariable Long parentLocationTypeId) throws ApplicationException {
		List<LocationTypeDto> childLocationType = locationService.getChildLocationsTypeOfParent(parentLocationTypeId);
		return childLocationType;
	}
	@RequestMapping(value = "/ajax/locationtype/save", method = RequestMethod.POST)
	public @ResponseBody LocationTypeDto saveLocationTypes(ModelAndView mv, @RequestBody LocationTypeDto locationTypeDto) throws ApplicationException {
		if(locationTypeDto.getParentLocationTypeId() == null){
			locationTypeDto = locationService.saveRootLocationType(locationTypeDto);	
		}else{
			locationTypeDto = locationService.saveLocationType(locationTypeDto);
		}
		return locationTypeDto;
	}

	@RequestMapping(value = "/ajax/location/getroot", method = RequestMethod.GET)
	public @ResponseBody LocationDto getRootLocationNode(ModelAndView mv) throws ApplicationException {
		LocationDto locationDto = locationService.getRootLocationForSwarajIndia();
		return locationDto;
	}
	
	@RequestMapping(value = "/ajax/location/getchild/{parentId}", method = RequestMethod.GET)
	public @ResponseBody List<LocationDto> getChildLocationNode(ModelAndView mv, @PathVariable Long parentId) throws ApplicationException {
		List<LocationDto> locationDtos = locationService.getChildLocationsOfParent(parentId);
		return locationDtos;
	}
	
	@RequestMapping(value = "/ajax/location/save", method = RequestMethod.POST)
	public @ResponseBody LocationDto saveState(ModelAndView mv, @RequestBody LocationDto locationDto) throws ApplicationException {
		locationDto = locationService.saveLocation(locationDto);
		return locationDto;
	}
	
	
    @RequestMapping(value = "/ajax/location/{locationId}/upload", method = RequestMethod.POST)
	public @ResponseBody String uploadLocationBoundaryFile(HttpServletRequest httpServletRequest, @PathVariable Long locationId) throws ApplicationException {
		try{
			System.out.println("locationId="+locationId);
            Collection<Part> parts = httpServletRequest.getParts();
            for (Part onePart : parts) {
                System.out.println("getContentType = " + onePart.getContentType());
                System.out.println("getName = " + onePart.getName());
                System.out.println("getSize = " + onePart.getSize());
                System.out.println("getSubmittedFileName = " + onePart.getSubmittedFileName());
                System.out.println("getHeaderNames = " + onePart.getHeaderNames());
            }
			Part uploadedImagePart = httpServletRequest.getPart("file");
			if(uploadedImagePart == null){
				throw new ApplicationException("Please choose a file");
			}
			//LocationBoundaryFileDto locationBoundaryFileDto = locationService.createNewLocationBoundaryFile(locationId, uploadedImagePart.getInputStream(), fileService);
			//TODO send the file to Kafka
			
			//photoDto.setOrgUrl(imageHttpUrl);
			//complaintService.addPhotoToComplaint(complaintDto.getId(), photoDto);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(uploadedImagePart.getInputStream());
			doc.getDocumentElement().normalize();
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			
			NodeList nList = doc.getElementsByTagName("SimpleData");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				 
				Node nNode = nList.item(temp);
		 
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
		 
				System.out.println("nNode.getNodeType : " + nNode.getNodeType());
				System.out.println("nNode.getAttributes : " + nNode.getAttributes());
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
					System.out.println("Name : " + eElement.getAttribute("name"));
					System.out.println("NodeValue : " + eElement.getNodeValue());
					System.out.println("getTextContent : " + eElement.getTextContent());
					System.out.println("getAttributes : " + eElement.getAttributes());
		 
				}
			}
			
			System.out.println("\n*********Coordinates********");
			NodeList coordinateList = doc.getElementsByTagName("coordinates");
			for (int temp = 0; temp < coordinateList.getLength(); temp++) {
				 
				Node nNode = coordinateList.item(temp);
		 
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
				System.out.println("nNode.getAttributes : " + nNode.getAttributes());
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
		 
					System.out.println("NodeValue : " + eElement.getNodeValue());
					System.out.println("getTextContent : " + eElement.getTextContent());
					System.out.println("getAttributes : " + eElement.getAttributes());
		 
				}
			}
				
		}catch(Exception ex){
			throw new ApplicationException(ex);
		}
		
		return "All Good";
		
	}
	/* TODO This function nee to move into strom cluster */
	private void computeAndSaveInRedis(String allCoordinates, Long locationId){
		String[] coordinates = allCoordinates.split(" ");
		String[] longLat;
		List<Long> locationList = new ArrayList<Long>();
		locationList.add(locationId);
		for(String oneCorrdinate : coordinates){
			longLat = oneCorrdinate.split(",");
			redisTemplate.opsForValue().set(buildKey(longLat[0], longLat[1]), locationList);	
		}
		
	}
	private String buildKey(String longitude, String lattitude){
		Double longi = Double.parseDouble(longitude);
		Double lati = Double.parseDouble(lattitude);
		Long iLong = (long)((longi * 10000));
		Long iLati = (long)((lati * 10000));
		return iLong +"."+iLati;
	}

}
