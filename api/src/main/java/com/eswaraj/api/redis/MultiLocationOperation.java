package com.eswaraj.api.redis;

import java.util.Collection;

import com.eswaraj.core.service.LocationKeyService;

public class MultiLocationOperation extends MultiLongKeyOperation {

    private LocationKeyService locationKeyService;

    public MultiLocationOperation(Collection<Long> locationIds, LocationKeyService locationKeyService) {
        super(locationIds);
        this.locationKeyService = locationKeyService;
    }

    @Override
    protected String buildKey(Long id) {
        return locationKeyService.getLocationInformationKey(id);
    }

}
