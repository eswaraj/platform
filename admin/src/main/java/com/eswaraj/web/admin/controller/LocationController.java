package com.eswaraj.web.admin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.FileService;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.web.dto.LocationBoundaryFileDto;
import com.eswaraj.web.dto.LocationDto;
import com.eswaraj.web.dto.LocationTypeDto;
import com.eswaraj.web.dto.LocationTypeJsonDto;

@Controller
public class LocationController extends BaseController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private FileService fileService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AppKeyService appKeyService;

    @Value("${aws_s3_directory_for_kml_files}")
    private String awsDirectoryForComplaintPhoto;

    @RequestMapping(value = "/location/upload/{locationId}", method = RequestMethod.POST)
    public @ResponseBody String handleFileUpload(HttpServletRequest httpServletRequest, @PathVariable Long locationId) throws IOException, ServletException, ApplicationException {
        System.out.println("locationId=" + locationId);
        Collection<Part> parts = httpServletRequest.getParts();
        for (Part onePart : parts) {
            System.out.println("getContentType = " + onePart.getContentType());
            System.out.println("getName = " + onePart.getName());
            System.out.println("getSize = " + onePart.getSize());
            System.out.println("getSubmittedFileName = " + onePart.getSubmittedFileName());
            System.out.println("getHeaderNames = " + onePart.getHeaderNames());
        }
        Part uploadedImagePart = httpServletRequest.getPart("file");
        if (uploadedImagePart == null) {
            throw new ApplicationException("Please choose a file");
        }
        LocationBoundaryFileDto locationBoundaryFileDto = locationService.createNewLocationBoundaryFile(locationId, uploadedImagePart.getSubmittedFileName(), uploadedImagePart.getInputStream(),
                fileService);


        return locationBoundaryFileDto.getFileNameAndPath();
    }

    @RequestMapping(value = "/ajax/locationtype/get", method = RequestMethod.GET)
    public @ResponseBody LocationTypeJsonDto getLocationTypes(ModelAndView mv) throws ApplicationException {
        LocationTypeJsonDto locationTypeJsonDto = locationService.getLocationTypes("beingIgnored");
        return locationTypeJsonDto;
    }

    @RequestMapping(value = "/ajax/locationtype/get/{locationTypeId}", method = RequestMethod.GET)
    public @ResponseBody LocationTypeDto getLocationTypes(ModelAndView mv, @PathVariable Long locationTypeId) throws ApplicationException {
        LocationTypeDto locationType = locationService.getLocationTypeById(locationTypeId);
        return locationType;
    }

    @RequestMapping(value = "/ajax/locationtype/getchild/{parentLocationTypeId}", method = RequestMethod.GET)
    public @ResponseBody List<LocationTypeDto> getLocationTypeChildren(ModelAndView mv, @PathVariable Long parentLocationTypeId) throws ApplicationException {
        List<LocationTypeDto> childLocationType = locationService.getChildLocationsTypeOfParent(parentLocationTypeId);
        return childLocationType;
    }

    @RequestMapping(value = "/ajax/locationtype/save", method = RequestMethod.POST)
    public @ResponseBody LocationTypeDto saveLocationTypes(ModelAndView mv, @RequestBody LocationTypeDto locationTypeDto) throws ApplicationException {
        if (locationTypeDto.getParentLocationTypeId() == null) {
            locationTypeDto = locationService.saveRootLocationType(locationTypeDto);
        } else {
            locationTypeDto = locationService.saveLocationType(locationTypeDto);
        }
        return locationTypeDto;
    }

    @RequestMapping(value = "/ajax/location/getroot", method = RequestMethod.GET)
    public @ResponseBody LocationDto getRootLocationNode(ModelAndView mv) throws ApplicationException {
        LocationDto locationDto = locationService.getRootLocationForSwarajIndia();
        return locationDto;
    }

    @RequestMapping(value = "/ajax/location/getpointlocations", method = RequestMethod.GET)
    public @ResponseBody List<LocationDto> getLocationAtPoint(HttpServletRequest httpServletRequest, ModelAndView mv) throws ApplicationException {
        System.out.println("Lat = " + Double.parseDouble(httpServletRequest.getParameter("lat")));
        System.out.println("Long = " + Double.parseDouble(httpServletRequest.getParameter("long")));
        String redisKey = appKeyService.buildLocationKey(Double.parseDouble(httpServletRequest.getParameter("lat")), Double.parseDouble(httpServletRequest.getParameter("long")));
        System.out.println("Redis Key = " + redisKey);
        Set<String> locations = stringRedisTemplate.opsForSet().members(redisKey);
        System.out.println("Redis Output = " + locations);
        List<LocationDto> returnList = new ArrayList<>(1);
        if (locations != null && !locations.isEmpty()) {
            List<Long> locationsLong = new ArrayList<>(locations.size());
            for (String oneLocationId : locations) {
                locationsLong.add(Long.parseLong(oneLocationId));
            }
            returnList = locationService.getLocations(locationsLong);
        }
        return returnList;
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
        try {
            System.out.println("locationId=" + locationId);
            Collection<Part> parts = httpServletRequest.getParts();
            for (Part onePart : parts) {
                System.out.println("getContentType = " + onePart.getContentType());
                System.out.println("getName = " + onePart.getName());
                System.out.println("getSize = " + onePart.getSize());
                System.out.println("getSubmittedFileName = " + onePart.getSubmittedFileName());
                System.out.println("getHeaderNames = " + onePart.getHeaderNames());
            }
            Part uploadedImagePart = httpServletRequest.getPart("file");
            if (uploadedImagePart == null) {
                throw new ApplicationException("Please choose a file");
            }
            LocationBoundaryFileDto locationBoundaryFileDto = locationService.createNewLocationBoundaryFile(locationId, uploadedImagePart.getSubmittedFileName(), uploadedImagePart.getInputStream(),
                    fileService);
            return locationBoundaryFileDto.getFileNameAndPath();
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }



    }

    @RequestMapping(value = "/ajax/location/search/name/{name}", method = RequestMethod.GET)
    public @ResponseBody List<LocationDto> searchLocation(HttpServletRequest httpServletRequest, ModelAndView mv, @PathVariable String name) throws ApplicationException {
        System.out.println("Searching Location for " + name);
        List<LocationDto> result = locationService.searchLocationByName(name);
        System.out.println("result =  " + result);
        return result;
    }

    @RequestMapping(value = "/ajax/location/{locationId}/kmlfiles", method = RequestMethod.GET)
    public @ResponseBody List<LocationBoundaryFileDto> getLocationFiles(HttpServletRequest httpServletRequest, ModelAndView mv, @PathVariable Long locationId) throws ApplicationException {
        List<LocationBoundaryFileDto> result = locationService.GetLocationAllBoundaryFile(locationId);
        return result;
    }

}
