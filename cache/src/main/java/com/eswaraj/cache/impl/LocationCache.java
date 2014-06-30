package com.eswaraj.cache.impl;

import org.springframework.stereotype.Component;

import com.eswaraj.web.dto.ComplaintDto;

@Component
public class LocationCache extends RedisCacheServiceImpl<String, ComplaintDto> {
	
}
