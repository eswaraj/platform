package com.eswaraj.api.redis;

import java.util.Collection;

import com.eswaraj.core.service.AppKeyService;

public class MultiLocationOperation extends MultiLongKeyOperation {

    private AppKeyService appKeyService;

    public MultiLocationOperation(Collection<Long> locationIds, AppKeyService appKeyService) {
        super(locationIds);
        this.appKeyService = appKeyService;
    }

    @Override
    protected String buildKey(Long id) {
        return appKeyService.getLocationInformationKey(id);
    }

}
