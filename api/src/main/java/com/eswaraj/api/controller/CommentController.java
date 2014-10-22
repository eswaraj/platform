package com.eswaraj.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.AppService;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

@Controller
public class CommentController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisTemplate<String, Long> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AppKeyService appKeyService;
    @Autowired
    private AppService appService;
    private JsonParser jsonParser = new JsonParser();

    @RequestMapping(value = "/api/v0/complaint/{complaintId}/comments", method = RequestMethod.GET)
    @ResponseBody
    public String getComplaintComments(HttpServletRequest httpServletRequest, ModelAndView mv, @PathVariable Long complaintId) throws ApplicationException {
        int start = getIntParameter(httpServletRequest, "start", 0);
        int count = getIntParameter(httpServletRequest, "count", 10);
        boolean adminOnly = getBooleanParameter(httpServletRequest, "admin_only", false);
        String redisSortedSetKey = appKeyService.getCommentListIdForComplaintKey(complaintId);
        if (adminOnly) {
            redisSortedSetKey = appKeyService.getAdminCommentListIdForComplaintKey(complaintId);
        }
        Set<String> commentIds = stringRedisTemplate.opsForZSet().range(redisSortedSetKey, start, start + count);
        if (commentIds == null || commentIds.isEmpty()) {
            return "[]";
        }
        List<String> commentKeys = new ArrayList<>(commentIds.size());
        for (String oneCommentId : commentIds) {
            commentKeys.add(appKeyService.getCommentIdKey(oneCommentId));
        }
        List<String> comments = stringRedisTemplate.opsForValue().multiGet(commentKeys);
        JsonArray commentArray = new JsonArray();
        for (String oneComment : comments) {
            commentArray.add(jsonParser.parse(oneComment));
        }
        return commentArray.toString();
    }

}
