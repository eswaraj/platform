package com.eswaraj.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.cache.CommentCache;
import com.eswaraj.core.exceptions.ApplicationException;

@Controller
public class CommentController extends BaseController {

    @Autowired
    private CommentCache commentCache;

    @RequestMapping(value = "/api/v0/complaint/{complaintId}/comments", method = RequestMethod.GET)
    @ResponseBody
    public String getComplaintComments(HttpServletRequest httpServletRequest, ModelAndView mv, @PathVariable Long complaintId) throws ApplicationException {
        int start = getIntParameter(httpServletRequest, "start", 0);
        int total = getIntParameter(httpServletRequest, "count", 10);
        boolean adminOnly = getBooleanParameter(httpServletRequest, "admin_only", false);
        String order = httpServletRequest.getParameter("order");

        String comments;
        if (adminOnly) {
            comments = commentCache.getComplaintAdminComments(complaintId, start, total, order);
        } else {
            comments = commentCache.getComplaintComments(complaintId, start, total, order);
        }
        return comments;
    }

}
