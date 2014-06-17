package com.eswaraj.web.admin.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.PoliticalBodyAdminDto;
import com.eswaraj.web.dto.PoliticalBodyTypeDto;

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
		politicalBodyAdminDto = appService.savePoliticalBodyAdmin(politicalBodyAdminDto);
		return politicalBodyAdminDto;
	}

}
