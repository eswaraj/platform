package com.eswaraj.web.admin.controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.service.CounterKeyService;

@Controller
public class StatsController {

    @Autowired
    private CounterKeyService counterKeyService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/stats.html", method = RequestMethod.GET)
	public ModelAndView showIndexPage(ModelAndView mv) {

        String globalPrefix = counterKeyService.getGlobalKeyPrefix();
        addDataToModel(mv, globalPrefix);
        mv.setViewName("stats");
		return mv;
	}

    private void addDataToModel(ModelAndView mv, String prefix) {
        Date currentDate = new Date();
        ValueOperations valueOperation = redisTemplate.opsForValue();
        long totalComplaints = (Long) valueOperation.get(counterKeyService.getTotalComplaintCounterKey(prefix));

        List<String> yearKeys = counterKeyService.getYearComplaintKeysForEternitySinceStart(prefix);
        List<Long> yearComplaints = valueOperation.multiGet(yearKeys);

        List<String> monthKeys = counterKeyService.getMonthComplaintKeysForTheYear(prefix, currentDate);
        List<Long> monthComplaints = valueOperation.multiGet(monthKeys);

        List<String> dayKeys = counterKeyService.getDayComplaintKeysForTheMonth(prefix, currentDate);
        List<Long> dayComplaints = valueOperation.multiGet(dayKeys);

        
        List<String> dayHourKeys = counterKeyService.getHourComplaintKeysForTheDay(prefix, currentDate);
        List<Long> dayHourComplaints = valueOperation.multiGet(dayHourKeys);
        

        List<String> last24HourKeys = counterKeyService.getHourComplaintKeysForLast24Hours(prefix, currentDate);
        List<Long> last24HourComplaints = valueOperation.multiGet(last24HourKeys);
        mv.getModel().put("totalComplaints", totalComplaints);
        mv.getModel().put("yearComplaints", mergeKeyAndValue(yearKeys, yearComplaints));
        mv.getModel().put("monthComplaints", mergeKeyAndValue(monthKeys, monthComplaints));
        mv.getModel().put("dayComplaints", mergeKeyAndValue(dayKeys, dayComplaints));
        mv.getModel().put("dayHourComplaints", mergeKeyAndValue(dayHourKeys, dayHourComplaints));
        mv.getModel().put("last24HourComplaints", mergeKeyAndValue(last24HourKeys, last24HourComplaints));

    }

    private Map<String, Long> mergeKeyAndValue(List<String> keys, List<Long> values) {
        Map<String, Long> map = new LinkedHashMap<>();
        int count = 0;
        for (String oneKey : keys) {
            map.put(oneKey, values.get(count));
            count++;
        }
        return map;
    }
}
