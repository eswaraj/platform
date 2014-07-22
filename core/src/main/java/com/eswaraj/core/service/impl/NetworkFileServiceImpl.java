package com.eswaraj.core.service.impl;

import java.io.InputStream;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.FileService;

public class NetworkFileServiceImpl implements FileService {

	@Override
	public String saveFile(String fileDir, String fileName, InputStream inputStream) throws ApplicationException {
		return "Not implemented yet";
	}

}
