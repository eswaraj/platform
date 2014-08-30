package com.eswaraj.web.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.PersonService;
import com.eswaraj.web.dto.PersonDto;

/**
 * @author ravi
 * @date Mar 01, 2014
 *
 */
@Controller
public class PersonController extends BaseController{

	@Autowired
	private PersonService personService;
	
	@RequestMapping(value = "/ajax/person/search/name/{searchParam}", method = RequestMethod.GET)
	@ResponseBody
	public List<PersonDto> searchPersonByName(ModelAndView mv, @PathVariable String searchParam) throws ApplicationException {
		return personService.searchPersonWithName(searchParam);
	}

	@RequestMapping(value = "/ajax/person/search/name", method = RequestMethod.GET)
	@ResponseBody
	public List<PersonDto> searchPersonByNameRequestParam(ModelAndView mv, @RequestParam("term") String term) throws ApplicationException {
		return personService.searchPersonWithName(term);
	}

	@RequestMapping(value = "/ajax/person/get/{personId}", method = RequestMethod.GET)
	@ResponseBody
	public PersonDto getPersonByid(ModelAndView mv, @PathVariable Long personId) throws ApplicationException {
		return personService.getPersonById(personId);
	}

	@RequestMapping(value = "/ajax/person/save", method = RequestMethod.POST)
	public @ResponseBody PersonDto savePerson(ModelAndView mv, @RequestBody PersonDto personDto) throws ApplicationException {
		personDto = personService.savePerson(personDto);
		return personDto;
	}


}
