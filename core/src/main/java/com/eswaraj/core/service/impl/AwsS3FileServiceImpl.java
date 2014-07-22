package com.eswaraj.core.service.impl;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.FileService;

public class AwsS3FileServiceImpl implements FileService {

	@Value("${aws_access_key}") 
	private String awsAccessKey;
	
	@Value("${aws_access_secret}") 
	private String awsAccessSecret;

	@Value("${aws_bucket_name}") 
	private String awsBucketName;
	
	@Value("${aws_s3_base_http_url}") 
	private String awsBaseHttpUrl;
	
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public String saveFile(String fileDir, String fileName, InputStream fileToUpload) throws ApplicationException {
		String remoteFileNameAndPath = fileDir + "/" +fileName;
		
		AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials(awsAccessKey, awsAccessSecret));

		logger.info("Uploading a new object to S3 from input Stream to remote file "+awsBucketName+"/"+remoteFileNameAndPath);
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setCacheControl("max-age=2592000");
		objectMetadata.addUserMetadata("x-amz-storage-class", "RRS");
		PutObjectRequest putObjectRequest = new PutObjectRequest(awsBucketName, remoteFileNameAndPath, fileToUpload, objectMetadata);
		putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
		s3client.putObject(putObjectRequest);
		String httpPath = awsBaseHttpUrl +"/"+ awsBucketName + "/"+remoteFileNameAndPath;
		logger.info("File Uploaded "+ httpPath);
		return httpPath;
	}

}
