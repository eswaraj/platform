package com.eswaraj.cache.redis.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.eswaraj.cache.CommentCache;
import com.eswaraj.cache.PersonCache;
import com.eswaraj.cache.PoliticalAdminCache;
import com.eswaraj.core.exceptions.ApplicationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Component
public class CommentCacheRedisImpl extends BaseCacheRedisImpl implements CommentCache {

    @Autowired
    @Qualifier("commentStringRedisTemplate")
    private StringRedisTemplate commentStringRedisTemplate;

    @Autowired
    private PersonCache personCache;

    @Autowired
    private PoliticalAdminCache politicalAdminCache;

    @Override
    public void refreshComplaintComment(long complaintId, long commentId) throws ApplicationException {
        JsonObject commentJsonObject = stormCacheAppServices.getComment(commentId);
        String commentKey = appKeyService.getCommentIdKey(commentId);
        String commentListKeyForComplaint = appKeyService.getCommentListIdForComplaintKey(complaintId);

        commentStringRedisTemplate.opsForValue().set(commentKey, commentJsonObject.toString());
        Long creationTime = commentJsonObject.get("creationTime").getAsLong();

        commentStringRedisTemplate.opsForZSet().add(commentListKeyForComplaint, String.valueOf(commentId), creationTime);

        boolean adminComment = commentJsonObject.get("adminComment").getAsBoolean();
        if (adminComment) {
            String adminOnlyCommentListForComplaint = appKeyService.getAdminCommentListIdForComplaintKey(complaintId);
            commentStringRedisTemplate.opsForZSet().add(adminOnlyCommentListForComplaint, String.valueOf(commentId), creationTime);
        }
    }

    @Override
    public String getComplaintComments(long complaintId, int start, int total, String order) throws ApplicationException {
        String redisSortedSetKey = appKeyService.getCommentListIdForComplaintKey(complaintId);
        Set<String> commentIds;
        if (order != null && order.equalsIgnoreCase("desc")) {
            commentIds = commentStringRedisTemplate.opsForZSet().reverseRange(redisSortedSetKey, start, start + total);
        } else {
            commentIds = commentStringRedisTemplate.opsForZSet().range(redisSortedSetKey, start, start + total);
        }

        JsonArray commentArray = getCommentByIds(commentIds);
        return commentArray.toString();
    }

    @Override
    public String getComplaintAdminComments(long complaintId, int start, int total, String order) throws ApplicationException {
        String redisSortedSetKey = appKeyService.getAdminCommentListIdForComplaintKey(complaintId);
        Set<String> commentIds;
        if (order != null && order.equalsIgnoreCase("desc")) {
            commentIds = commentStringRedisTemplate.opsForZSet().reverseRange(redisSortedSetKey, start, start + total);
        } else {
            commentIds = commentStringRedisTemplate.opsForZSet().range(redisSortedSetKey, start, start + total);
        }
        JsonArray commentArray = getCommentByIds(commentIds);

        return commentArray.toString();
    }

    private JsonArray getCommentByIds(Set<String> commentIds) throws ApplicationException {
        JsonArray commentArray = new JsonArray();
        if (commentIds == null || commentIds.isEmpty()) {
            return commentArray;
        }
        List<String> commentKeys = new ArrayList<>(commentIds.size());
        for (String oneCommentId : commentIds) {
            commentKeys.add(appKeyService.getCommentIdKey(oneCommentId));
        }
        List<String> comments = commentStringRedisTemplate.opsForValue().multiGet(commentKeys);
        for (String oneComment : comments) {
            commentArray.add(jsonParser.parse(oneComment));
        }
        addPersonDetail(commentArray);
        return commentArray;
    }

    private void addPersonDetail(JsonArray commentArray) throws ApplicationException {
        JsonObject jsonObject;
        List<Long> personIds = new ArrayList<>(commentArray.size());
        for (int i = 0; i < commentArray.size(); i++) {
            jsonObject = commentArray.get(i).getAsJsonObject();
            Long personId = jsonObject.get("commentedById").getAsLong();
            personIds.add(personId);
        }
        
        List<String> persons = personCache.getPersonsByIds(personIds);
        int count = 0;
        for (String onePerson : persons) {
            jsonObject = commentArray.get(count).getAsJsonObject();
            jsonObject.remove("commentedById");
            if (onePerson != null) {
                jsonObject.add("postedBy", jsonParser.parse(onePerson));
            }
            count++;
        }

    }

    private void addPoliticalAdminDetail(JsonArray commentArray) throws ApplicationException {
        JsonObject jsonObject;
        List<String> politicalAdminIds = new ArrayList<>(commentArray.size());
        for (int i = 0; i < commentArray.size(); i++) {
            jsonObject = commentArray.get(i).getAsJsonObject();
            String politicalAdminId;
            if (jsonObject.get("politicalAdminId") == null) {
                politicalAdminId = "0";
            } else {
                politicalAdminId = jsonObject.get("politicalAdminId").getAsString();
            }
            politicalAdminIds.add(politicalAdminId);
        }

        JsonArray admins = politicalAdminCache.getPoliticalBodyAdminByIds(politicalAdminIds);
        JsonObject oneAdmin;
        for (int count = 0; count < admins.size(); count++) {
            jsonObject = commentArray.get(count).getAsJsonObject();
            jsonObject.remove("politicalAdminId");
            oneAdmin = admins.get(count).getAsJsonObject();
            if (oneAdmin != null) {
                jsonObject.add("admin", oneAdmin);
            }
            count++;
        }

    }

    @Override
    public long getComplaintCommentCount(long complaintId) throws ApplicationException {
        String commentListKeyForComplaint = appKeyService.getCommentListIdForComplaintKey(complaintId);
        return commentStringRedisTemplate.opsForZSet().size(commentListKeyForComplaint);
    }

}
