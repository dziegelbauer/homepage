package org.ziegelbauer.homepage.data;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Repository;

import java.io.InputStream;

@Repository
public class AWSS3Repository {
    public void uploadFile(String fileName, InputStream inputStream, String s3BucketName) {
        AmazonS3Client client = (AmazonS3Client) AmazonS3Client.builder().build();

        PutObjectRequest request = new PutObjectRequest(
                s3BucketName,
                fileName,
                inputStream,
                null
        );

        client.putObject(request);
    }

    public void deleteFile(String fileName, String s3BucketName) {
        AmazonS3Client client = (AmazonS3Client) AmazonS3Client.builder().build();

        client.deleteObject(s3BucketName, fileName);
    }
}
