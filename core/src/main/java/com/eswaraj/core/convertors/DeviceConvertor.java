package com.eswaraj.core.convertors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Device;
import com.eswaraj.domain.repo.DeviceRepository;
import com.eswaraj.web.dto.DeviceDto;

@Component
public class DeviceConvertor extends BaseConvertor<Device, DeviceDto> {

    private static final long serialVersionUID = 1L;
    @Autowired
    private DeviceRepository deviceRepository;
	

	@Override
    protected Device convertInternal(DeviceDto deviceDto) throws ApplicationException {
        Device device = getObjectIfExists(deviceDto, "Device", deviceRepository);
        if (device == null) {
            device = new Device();
		}
        BeanUtils.copyProperties(deviceDto, device);
        return device;
	}

	@Override
    protected DeviceDto convertBeanInternal(Device dbDto) {
        DeviceDto deviceDto = new DeviceDto();
        BeanUtils.copyProperties(dbDto, deviceDto);
        return deviceDto;
	}

}
