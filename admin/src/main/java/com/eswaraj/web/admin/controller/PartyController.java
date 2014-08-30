package com.eswaraj.web.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.web.dto.PartyDto;

/**
 * @author ravi
 * @date Mar 01, 2014
 *
 */
@Controller
public class PartyController extends BaseController{

	@Autowired
	private AppService appService;
	
	@RequestMapping(value = "/ajax/party/getall", method = RequestMethod.GET)
	@ResponseBody
	public List<PartyDto> getAllParties(ModelAndView mv) throws ApplicationException {
		return appService.getAllPoliticalParties();
	}

	@RequestMapping(value = "/ajax/party/save", method = RequestMethod.POST)
	public @ResponseBody PartyDto saveParty(ModelAndView mv, @RequestBody PartyDto partyDto) throws ApplicationException {
		partyDto = appService.saveParty(partyDto);
		return partyDto;
	}


}
