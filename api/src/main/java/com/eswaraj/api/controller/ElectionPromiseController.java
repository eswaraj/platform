package com.eswaraj.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.ElectionPromiseService;
import com.eswaraj.web.dto.v1.ElectionManifestoPromiseDto;

@Controller
public class ElectionPromiseController extends BaseController {

    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ElectionPromiseService electionPromiseService;


    @RequestMapping(value = "/api/v0/promises/{politicalAdminId}", method = RequestMethod.GET)
    public @ResponseBody List<ElectionManifestoPromiseDto> getAllCategories(ModelAndView mv, HttpServletRequest httpServletRequest, @PathVariable Long politicalAdminId) throws ApplicationException {
        return electionPromiseService.getElectionPromisesByPoliticalAdminId(politicalAdminId);
    }

}
