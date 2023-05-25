package com.mate.helgather.repository;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
public class AmazonS3Repository {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;

    // /{dir}/fileName.{extention} 형태
    public String save(File file, String fullPath) throws Exception {
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fullPath, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3Client.getUrl(bucket, fullPath).toString();
        } catch (AmazonS3Exception e) {
            throw e;
        } finally {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    public void delete(String path) throws Exception {
        try {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, path));
        } catch (AmazonServiceException e) {
            throw new AmazonS3Exception("아마존 S3 에러");
        }
    }
}
