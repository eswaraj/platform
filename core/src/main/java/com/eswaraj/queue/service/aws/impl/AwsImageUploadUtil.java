package com.eswaraj.queue.service.aws.impl;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

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
import com.amazonaws.services.s3.model.StorageClass;

@Component
public class AwsImageUploadUtil {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //
    @PostConstruct
    public void init() {
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

    @Value("${category_pic_base_directory}")
    private String categoryPicBaseDirectory;

    @Value("${location_pic_base_directory:location}")
    private String locationPicBaseDirectory;

    @Value("${s3_bucket}")
    private String s3Bucket;

    @Value("${s3_base_http_path_profile_pic}")
    private String s3BaseHttpForProfilePic;

    private void uploadFileToS3(String awsKey, String awsSecret, String bucketName, String remoteFileNameAndPath, String localFilePathToUpload) throws FileNotFoundException {

        AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials(awsKey, awsSecret));

        logger.info("Uploading a new object to S3 from " + localFilePathToUpload + " to remote file " + bucketName + "/" + remoteFileNameAndPath);
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

    private void uploadFileToS3(String awsKey, String awsSecret, String bucketName, String remoteFileNameAndPath, InputStream fileToUpload, String contentType) throws FileNotFoundException {

        AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials(awsKey, awsSecret));

        logger.info("Uploading a new object to S3 from input Stream to remote file " + bucketName + "/" + remoteFileNameAndPath);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setCacheControl("max-age=2592000");
        objectMetadata.setContentType(contentType);
        objectMetadata.addUserMetadata("x-amz-storage-class", "RRS");

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, remoteFileNameAndPath, fileToUpload, objectMetadata);
        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        putObjectRequest.setStorageClass(StorageClass.Standard);
        s3client.putObject(putObjectRequest);
        logger.info("File Uploaded");
    }

    public String uploadProfileImageJpeg(String remoteFileName, InputStream localFilePathToUpload) throws FileNotFoundException {
        String remoteFileNameAndPath = profilePicBaseDirectory + "/" + remoteFileName;
        uploadFileToS3(accessKey, accessSecret, s3Bucket, remoteFileNameAndPath, localFilePathToUpload, "image/jpeg");
        String httpPath = s3BaseHttpForProfilePic + "/" + profilePicBaseDirectory + "/" + remoteFileName;
        return httpPath;
    }

    public String uploadCategoryImage(String remoteFileName, InputStream localFilePathToUpload, String imageType) throws FileNotFoundException {
        String contentTypeHeader = "image/jpeg";
        if (".png".equalsIgnoreCase(imageType)) {
            contentTypeHeader = "image/png";
        }
        String remoteFileNameAndPath = categoryPicBaseDirectory + "/" + remoteFileName;
        uploadFileToS3(accessKey, accessSecret, s3Bucket, remoteFileNameAndPath, localFilePathToUpload, contentTypeHeader);
        String httpPath = s3BaseHttpForProfilePic + "/" + categoryPicBaseDirectory + "/" + remoteFileName;
        return httpPath;
    }

    public String uploadLocationHeaderImage(String remoteFileName, InputStream localFilePathToUpload, String imageType) throws FileNotFoundException {
        String contentTypeHeader = "image/jpeg";
        if (".png".equalsIgnoreCase(imageType)) {
            contentTypeHeader = "image/png";
        }
        String remoteFileNameAndPath = locationPicBaseDirectory + "/" + remoteFileName;
        uploadFileToS3(accessKey, accessSecret, s3Bucket, remoteFileNameAndPath, localFilePathToUpload, contentTypeHeader);
        String httpPath = s3BaseHttpForProfilePic + "/" + locationPicBaseDirectory + "/" + remoteFileName;
        return httpPath;
    }

    public static BufferedImage resize(InputStream is, int newW, int newH) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(is);
        return resize(bufferedImage, newW, newH);
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }

}
