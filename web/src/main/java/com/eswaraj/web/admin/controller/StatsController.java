package com.eswaraj.web.admin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.web.dto.CategoryWithChildCategoryDto;
import com.eswaraj.web.dto.LocationDto;

@Controller
public class StatsController {

    @Autowired
    private AppKeyService appKeyService;
    @Autowired
    private AppService appService;
    @Autowired
    private LocationService locationService;

    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @RequestMapping(value = "/stats.html", method = RequestMethod.GET)
    public ModelAndView showIndexPage(ModelAndView mv) throws ApplicationException {

        String globalPrefix = appKeyService.getGlobalKeyPrefix();
        // addDataToModel(mv, globalPrefix);
        // addCategoryStats(mv);
        mv.setViewName("stats");
        return mv;
    }

    @RequestMapping(value = "/global.html", method = RequestMethod.GET)
    public ModelAndView showGlobalPage(ModelAndView mv) throws ApplicationException {

        String globalPrefix = appKeyService.getGlobalKeyPrefix();
        addDataToModel(mv, globalPrefix);
        addCategoryAllTimeStats(mv, null);

        mv.getModel().put("title", "Gloabl Stats");
        LocationDto india = locationService.getRootLocationForSwarajIndia();
        List<LocationDto> childLocations = locationService.getChildLocationsOfParent(india.getId());
        mv.getModel().put("locations", childLocations);

        mv.setViewName("global");
		return mv;
	}

    @RequestMapping(value = "/stat/location/{locationId}.html", method = RequestMethod.GET)
    public ModelAndView showStateLocations(ModelAndView mv, @PathVariable Long locationId) throws ApplicationException {

        String locationPrefix = appKeyService.getLocationCounterKey(locationId);
        addDataToModel(mv, locationPrefix);
        addCategoryAllTimeStats(mv, locationId);
        List<LocationDto> childLocations = locationService.getChildLocationsOfParent(locationId);
        LocationDto location = locationService.getLocationById(locationId);
        mv.getModel().put("title", "Stats for " + location.getName());
        mv.getModel().put("locations", childLocations);
        mv.setViewName("global");
        return mv;
    }

    private void addDataToModel(ModelAndView mv, String prefix) throws ApplicationException {
        try {
            Date currentDate = new Date();
            ValueOperations<String, String> valueOperation = redisTemplate.opsForValue();
            String totalComplaints = valueOperation.get(appKeyService.getTotalComplaintCounterKey(prefix));

            List<String> yearKeys = appKeyService.getYearComplaintKeysForEternitySinceStart(prefix);
            List<String> yearComplaints = valueOperation.multiGet(yearKeys);

            List<String> monthKeys = appKeyService.getMonthComplaintKeysForTheYear(prefix, currentDate);
            List<String> monthComplaints = valueOperation.multiGet(monthKeys);

            List<String> dayKeys = appKeyService.getDayComplaintKeysForTheMonth(prefix, currentDate);
            List<String> dayComplaints = valueOperation.multiGet(dayKeys);

            List<String> dayHourKeys = appKeyService.getHourComplaintKeysForTheDay(prefix, currentDate);
            List<String> dayHourComplaints = valueOperation.multiGet(dayHourKeys);

            List<String> last24HourKeys = appKeyService.getHourComplaintKeysForLast24Hours(prefix, currentDate);
            List<String> last24HourComplaints = valueOperation.multiGet(last24HourKeys);


            mv.getModel().put("totalComplaints", totalComplaints);
            mv.getModel().put("yearComplaints", mergeKeyAndValue(yearKeys, yearComplaints));
            mv.getModel().put("monthComplaints", mergeKeyAndValue(monthKeys, monthComplaints));
            mv.getModel().put("dayComplaints", mergeKeyAndValue(dayKeys, dayComplaints));
            mv.getModel().put("dayHourComplaints", mergeKeyAndValue(dayHourKeys, dayHourComplaints));
            mv.getModel().put("last24HourComplaints", mergeKeyAndValue(last24HourKeys, last24HourComplaints));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void addCategoryAllTimeStats(ModelAndView mv, Long locationId) throws ApplicationException {
        try {
            List<CategoryWithChildCategoryDto> categories = appService.getAllCategories();

            Date currentDate = new Date();
            List<String> categoryName = new ArrayList<>();
            List<String> categoryTotalKeys = new ArrayList<>();
            for (CategoryWithChildCategoryDto oneCategoryWithChildCategoryDto : categories) {
                categoryName.add(oneCategoryWithChildCategoryDto.getName());
                String prefix = appKeyService.getCategoryKey(oneCategoryWithChildCategoryDto.getId());
                categoryTotalKeys.add(appKeyService.getTotalComplaintCounterKey(prefix));
                if (oneCategoryWithChildCategoryDto.getChildCategories() != null) {
                    for (CategoryWithChildCategoryDto oneChildCategoryWithChildCategoryDto : oneCategoryWithChildCategoryDto.getChildCategories()) {
                        categoryName.add(oneCategoryWithChildCategoryDto.getName() + "  -->  " + oneChildCategoryWithChildCategoryDto.getName());
                        if (locationId == null) {
                            prefix = appKeyService.getCategoryKey(oneChildCategoryWithChildCategoryDto.getId());
                        } else {
                            prefix = appKeyService.getLocationCategoryKeyPrefix(locationId, oneChildCategoryWithChildCategoryDto.getId());
                        }

                        categoryTotalKeys.add(appKeyService.getTotalComplaintCounterKey(prefix));
                    }
                }
            }
            ValueOperations<String, String> valueOperation = redisTemplate.opsForValue();
            System.out.println("Total Category keys = " + categoryTotalKeys.size());
            List<String> totalComplaints = valueOperation.multiGet(categoryTotalKeys);

            mv.getModel().put("totalCategoryComplaints", mergeKeyAndValue(categoryName, totalComplaints));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

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
            String prefix = appKeyService.getCategoryKey(oneCategoryWithChildCategoryDto.getId());
            categoryHourlyKeys.add(appKeyService.getCategoryHourComplaintCounterKey(currentDate, oneCategoryWithChildCategoryDto.getId()));
            categoryDayKeys.add(appKeyService.getDayComplaintCounterKey(prefix, currentDate));
            category24HourKeys.add(appKeyService.getLast24HourComplaintCounterKey(prefix, currentDate));
            categoryMonthKeys.add(appKeyService.getMonthComplaintCounterKey(prefix, currentDate));
            categoryYearKeys.add(appKeyService.getYearComplaintCounterKey(prefix, currentDate));
            categoryTotalKeys.add(appKeyService.getTotalComplaintCounterKey(prefix));
            if (oneCategoryWithChildCategoryDto.getChildCategories() != null) {
                for (CategoryWithChildCategoryDto oneChildCategoryWithChildCategoryDto : oneCategoryWithChildCategoryDto.getChildCategories()) {
                    categoryName.add(oneChildCategoryWithChildCategoryDto.getName());
                    prefix = appKeyService.getCategoryKey(oneChildCategoryWithChildCategoryDto.getId());
                    categoryHourlyKeys.add(appKeyService.getCategoryHourComplaintCounterKey(currentDate, oneChildCategoryWithChildCategoryDto.getId()));
                    categoryDayKeys.add(appKeyService.getDayComplaintCounterKey(prefix, currentDate));
                    category24HourKeys.add(appKeyService.getLast24HourComplaintCounterKey(prefix, currentDate));
                    categoryMonthKeys.add(appKeyService.getMonthComplaintCounterKey(prefix, currentDate));
                    categoryYearKeys.add(appKeyService.getYearComplaintCounterKey(prefix, currentDate));
                    categoryTotalKeys.add(appKeyService.getTotalComplaintCounterKey(prefix));
                }
            }
        }
        ValueOperations<String, String> valueOperation = redisTemplate.opsForValue();

        List<String> totalComplaints = valueOperation.multiGet(categoryTotalKeys);

        List<String> yearComplaints = valueOperation.multiGet(categoryYearKeys);

        List<String> monthComplaints = valueOperation.multiGet(categoryMonthKeys);

        List<String> dayComplaints = valueOperation.multiGet(categoryDayKeys);

        List<String> dayHourComplaints = valueOperation.multiGet(categoryHourlyKeys);

        List<String> last24HourComplaints = valueOperation.multiGet(category24HourKeys);

        mv.getModel().put("totalCategoryComplaints", mergeKeyAndValue(categoryName, yearComplaints));
        mv.getModel().put("yearCategoryComplaints", mergeKeyAndValue(categoryName, yearComplaints));
        mv.getModel().put("monthCategoryComplaints", mergeKeyAndValue(categoryName, monthComplaints));
        mv.getModel().put("dayCategoryComplaints", mergeKeyAndValue(categoryName, dayComplaints));
        mv.getModel().put("dayHourCategoryComplaints", mergeKeyAndValue(categoryName, dayHourComplaints));
        mv.getModel().put("last24HourCategoryComplaints", mergeKeyAndValue(categoryName, last24HourComplaints));

    }

    private Map<String, String> mergeKeyAndValue(List<String> keys, List<String> values) {
        System.out.println("Total Categories " + keys.size());
        System.out.println("Total Counters " + values.size());
        Map<String, String> map = new LinkedHashMap<>();
        int count = 0;
        for (String oneKey : keys) {
            try {
                map.put(oneKey, values.get(count));
            } catch (Exception ex) {

            }

            count++;
        }
        return map;
    }
}
