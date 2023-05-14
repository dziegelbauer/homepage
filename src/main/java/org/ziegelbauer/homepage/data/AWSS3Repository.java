package org.ziegelbauer.homepage.data;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Repository
public class AWSS3Repository {
    public void uploadFile(String fileName, InputStream inputStream, String s3BucketName) throws IOException {
        AmazonS3Client client = (AmazonS3Client) AmazonS3Client.builder().build();

        byte[] imageData = inputStream.readAllBytes();
        inputStream.close();

        InputStream memoryStream = new ByteArrayInputStream(imageData);

        ObjectMetadata metaData = new ObjectMetadata();

        metaData.setContentLength(imageData.length);

        log.info("Uploading file: {}", fileName);
        client.putObject(
                s3BucketName,
                fileName,
                memoryStream,
                metaData
        );
        memoryStream.close();
        log.info("Upload complete");
    }

    public void deleteFile(String fileName, String s3BucketName) {
        AmazonS3Client client = (AmazonS3Client) AmazonS3Client.builder().build();

        client.deleteObject(s3BucketName, fileName);
    }
}
