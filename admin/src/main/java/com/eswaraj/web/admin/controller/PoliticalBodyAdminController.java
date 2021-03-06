package com.eswaraj.web.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.PoliticalBodyAdminDto;
import com.eswaraj.web.dto.PoliticalBodyAdminStaffDto;
import com.eswaraj.web.dto.SavePoliticalAdminStaffRequestDto;

@Controller
public class PoliticalBodyAdminController extends BaseController {

	
	@RequestMapping(value = "/ajax/pbadmin/get/{locationId}/{pbTypeId}", method = RequestMethod.GET)
	public @ResponseBody List<PoliticalBodyAdminDto> getPoliticalBodyAdmins(ModelAndView mv, @PathVariable Long locationId,@PathVariable Long pbTypeId) throws ApplicationException {
		List<PoliticalBodyAdminDto> politicalBodyAdmins = appService.getAllPoliticalBodyAdminByLocationId(locationId, pbTypeId);
		return politicalBodyAdmins;
	}
	
	@RequestMapping(value = "/ajax/pbadmin/getcurrent/{locationId}/{pbTypeId}", method = RequestMethod.GET)
	public @ResponseBody PoliticalBodyAdminDto getCurrentPoliticalBodyAdminOfLocation(ModelAndView mv, @PathVariable Long locationId,@PathVariable Long pbTypeId) throws ApplicationException {
		PoliticalBodyAdminDto politicalBodyAdminDto = appService.getCurrentPoliticalBodyAdminByLocationId(locationId, pbTypeId);
		return politicalBodyAdminDto;
	}

	@RequestMapping(value = "/ajax/pbadmin/save", method = RequestMethod.POST)
	public @ResponseBody PoliticalBodyAdminDto savePoliticalBodyAdmin(ModelAndView mv, @RequestBody PoliticalBodyAdminDto politicalBodyAdminDto) throws ApplicationException {
        logger.info("Saving Political Body Admin : {}", politicalBodyAdminDto);
		politicalBodyAdminDto = appService.savePoliticalBodyAdmin(politicalBodyAdminDto);
		return politicalBodyAdminDto;
	}

    @RequestMapping(value = "/ajax/pbadmin/history/get/{personId}", method = RequestMethod.GET)
    public @ResponseBody List<PoliticalBodyAdminDto> getPoliticalBodyAdminHistoryOfPerson(ModelAndView mv, @PathVariable Long personId) throws ApplicationException {
        List<PoliticalBodyAdminDto> politicalBodyAdmins = appService.getAllPoliticalBodyAdminHistoryByPersonId(personId);
        return politicalBodyAdmins;
    }

    @RequestMapping(value = "/ajax/leader/staff", method = RequestMethod.POST)
    public @ResponseBody List<PoliticalBodyAdminStaffDto> saveLeaderStaff(HttpServletRequest httpServletRequest, ModelAndView mv,
            @RequestBody SavePoliticalAdminStaffRequestDto savePoliticalAdminStaffRequestDto) throws ApplicationException {
        appService.savePoliticalBodyAdminStaff(savePoliticalAdminStaffRequestDto);
        List<PoliticalBodyAdminStaffDto> politicalBodyAdminStaffs = appService.getAllStaffOfPoliticalAdmin(savePoliticalAdminStaffRequestDto.getPoliticalAdminId());
        return politicalBodyAdminStaffs;
    }

    @RequestMapping(value = "/ajax/leader/staff/{politicalAdminId}", method = RequestMethod.GET)
    public @ResponseBody List<PoliticalBodyAdminStaffDto> getLeaderStaff(HttpServletRequest httpServletRequest, ModelAndView mv, @PathVariable Long politicalAdminId) throws ApplicationException {
        List<PoliticalBodyAdminStaffDto> politicalBodyAdminStaffs = appService.getAllStaffOfPoliticalAdmin(politicalAdminId);
        return politicalBodyAdminStaffs;
    }

    @RequestMapping(value = "/ajax/leader/staff/{politicalAdminStaffId}", method = RequestMethod.DELETE)
    public @ResponseBody PoliticalBodyAdminStaffDto deleteLeaderStaff(HttpServletRequest httpServletRequest, ModelAndView mv, @PathVariable Long politicalAdminStaffId) throws ApplicationException {
        PoliticalBodyAdminStaffDto politicalBodyAdminStaffDto = appService.deletePoliticalAdminStaff(politicalAdminStaffId);
        return politicalBodyAdminStaffDto;
    }

}
