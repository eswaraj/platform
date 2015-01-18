package com.eswaraj.queue.service.aws.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

@Component
public class AwsImageUploadUtil {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	//
	@PostConstruct
	public void init(){
		System.out.println("Created AwsImageUtil");
	}
	
    @Value("${aws_access_key}")
    private String accessKey;

    @Value("${aws_access_secret}")
    private String accessSecret;

    @Value("${aws_region}")
    private String awsRegion;

    @Value("${profile_pic_base_directory}")
    private String profilePicBaseDirectory;

    @Value("${s3_bucket}")
    private String s3Bucket;

    @Value("${s3_base_http_path_profile_pic}")
    private String s3BaseHttpForProfilePic;

	private void uploadFileToS3(String awsKey, String awsSecret, String bucketName, String remoteFileNameAndPath, String localFilePathToUpload) throws FileNotFoundException {

		AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials(awsKey, awsSecret));

		logger.info("Uploading a new object to S3 from "+localFilePathToUpload+" to remote file "+bucketName+"/"+remoteFileNameAndPath);
		FileInputStream file = new FileInputStream(localFilePathToUpload);
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setCacheControl("max-age=2592000");
		objectMetadata.addUserMetadata("x-amz-storage-class", "RRS");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 2);
		objectMetadata.setExpirationTime(calendar.getTime());
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, remoteFileNameAndPath, file, objectMetadata);
		putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
		PutObjectResult putObjectResult = s3client.putObject(putObjectRequest);
		logger.info("File Uploaded");
	}
	
	private void uploadFileToS3(String awsKey, String awsSecret, String bucketName, String remoteFileNameAndPath, InputStream fileToUpload) throws FileNotFoundException {

		AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials(awsKey, awsSecret));

		logger.info("Uploading a new object to S3 from input Stream to remote file "+bucketName+"/"+remoteFileNameAndPath);
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setCacheControl("max-age=2592000");
		objectMetadata.addUserMetadata("x-amz-storage-class", "RRS");
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, remoteFileNameAndPath, fileToUpload, objectMetadata);
		putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
		s3client.putObject(putObjectRequest);
		logger.info("File Uploaded");
	}

	public String uploadProfileImage(String remoteFileName, InputStream localFilePathToUpload) throws FileNotFoundException {
        String remoteFileNameAndPath = profilePicBaseDirectory + "/" + remoteFileName;
        uploadFileToS3(accessKey, accessSecret, s3Bucket, remoteFileNameAndPath, localFilePathToUpload);
        String httpPath = s3BaseHttpForProfilePic + "/" + profilePicBaseDirectory + "/" + remoteFileName;
		return httpPath;
	}
}
