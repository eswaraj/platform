package com.eswaraj.core.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.FileService;

public class LocalFileServiceImpl implements FileService {

	@Override
	public String saveFile(String fileDir, String fileName, InputStream inputStream) throws ApplicationException {
		Path path = Paths.get(fileDir, fileName);
		try {
			if(!fileDir.endsWith("/")){
				fileDir = fileDir + "/";
			}
			Files.copy(inputStream, path);
		} catch (IOException e) {
			throw new ApplicationException("Unable to copy file to "+path.toString(),e);
		}
		return "";
	}

}
