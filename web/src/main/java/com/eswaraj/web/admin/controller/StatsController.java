package com.eswaraj.web.admin.controller;

import java.util.ArrayList;
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

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.CounterKeyService;
import com.eswaraj.web.dto.CategoryWithChildCategoryDto;

@Controller
public class StatsController {

    @Autowired
    private CounterKeyService counterKeyService;
    @Autowired
    private AppService appService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/stats.html", method = RequestMethod.GET)
    public ModelAndView showIndexPage(ModelAndView mv) throws ApplicationException {

        String globalPrefix = counterKeyService.getGlobalKeyPrefix();
        addDataToModel(mv, globalPrefix);
        addCategoryStats(mv);
        mv.setViewName("stats");
		return mv;
	}

    private void addDataToModel(ModelAndView mv, String prefix) throws ApplicationException {
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

    private void addCategoryStats(ModelAndView mv) throws ApplicationException {
        List<CategoryWithChildCategoryDto> categories = appService.getAllCategories();

        Date currentDate = new Date();
        List<String> categoryName = new ArrayList<>();
        List<String> categoryHourlyKeys = new ArrayList<>();
        List<String> category24HourKeys = new ArrayList<>();
        List<String> categoryDayKeys = new ArrayList<>();
        List<String> categoryMonthKeys = new ArrayList<>();
        List<String> categoryYearKeys = new ArrayList<>();
        List<String> categoryTotalKeys = new ArrayList<>();
        for (CategoryWithChildCategoryDto oneCategoryWithChildCategoryDto : categories) {
            categoryName.add(oneCategoryWithChildCategoryDto.getName());
            String prefix = counterKeyService.getCategoryKeyPrefix(oneCategoryWithChildCategoryDto.getId());
            categoryHourlyKeys.add(counterKeyService.getCategoryHourComplaintCounterKey(currentDate, oneCategoryWithChildCategoryDto.getId()));
            categoryDayKeys.add(counterKeyService.getDayComplaintCounterKey(prefix, currentDate));
            category24HourKeys.add(counterKeyService.getLast24HourComplaintCounterKey(prefix, currentDate));
            categoryMonthKeys.add(counterKeyService.getMonthComplaintCounterKey(prefix, currentDate));
            categoryYearKeys.add(counterKeyService.getYearComplaintCounterKey(prefix, currentDate));
            categoryTotalKeys.add(counterKeyService.getTotalComplaintCounterKey(prefix));
            if (oneCategoryWithChildCategoryDto.getChildCategories() != null) {
                for (CategoryWithChildCategoryDto oneChildCategoryWithChildCategoryDto : oneCategoryWithChildCategoryDto.getChildCategories()) {
                    categoryName.add(oneChildCategoryWithChildCategoryDto.getName());
                    prefix = counterKeyService.getCategoryKeyPrefix(oneChildCategoryWithChildCategoryDto.getId());
                    categoryHourlyKeys.add(counterKeyService.getCategoryHourComplaintCounterKey(currentDate, oneChildCategoryWithChildCategoryDto.getId()));
                    categoryDayKeys.add(counterKeyService.getDayComplaintCounterKey(prefix, currentDate));
                    category24HourKeys.add(counterKeyService.getLast24HourComplaintCounterKey(prefix, currentDate));
                    categoryMonthKeys.add(counterKeyService.getMonthComplaintCounterKey(prefix, currentDate));
                    categoryYearKeys.add(counterKeyService.getYearComplaintCounterKey(prefix, currentDate));
                    categoryTotalKeys.add(counterKeyService.getTotalComplaintCounterKey(prefix));
                }
            }
        }
        ValueOperations valueOperation = redisTemplate.opsForValue();

        List<Long> totalComplaints = valueOperation.multiGet(categoryTotalKeys);

        List<Long> yearComplaints = valueOperation.multiGet(categoryYearKeys);

        List<Long> monthComplaints = valueOperation.multiGet(categoryMonthKeys);

        List<Long> dayComplaints = valueOperation.multiGet(categoryDayKeys);

        List<Long> dayHourComplaints = valueOperation.multiGet(categoryHourlyKeys);

        List<Long> last24HourComplaints = valueOperation.multiGet(category24HourKeys);

        mv.getModel().put("totalCategoryComplaints", mergeKeyAndValue(categoryName, yearComplaints));
        mv.getModel().put("yearCategoryComplaints", mergeKeyAndValue(categoryName, yearComplaints));
        mv.getModel().put("monthCategoryComplaints", mergeKeyAndValue(categoryName, monthComplaints));
        mv.getModel().put("dayCategoryComplaints", mergeKeyAndValue(categoryName, dayComplaints));
        mv.getModel().put("dayHourCategoryComplaints", mergeKeyAndValue(categoryName, dayHourComplaints));
        mv.getModel().put("last24HourCategoryComplaints", mergeKeyAndValue(categoryName, last24HourComplaints));

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