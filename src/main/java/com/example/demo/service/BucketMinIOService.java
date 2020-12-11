package com.example.demo.service;

import com.example.demo.model.DiretoriosMinIO;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.RemoveBucketArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BucketMinIOService {

    @Autowired
    private MinioClient minioClient;

    public List<DiretoriosMinIO> getAllBuckets() {
        try {
            return minioClient.listBuckets().stream()
                    .map(bucket -> new DiretoriosMinIO(bucket.name(), bucket.creationDate().toString()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String delete(String nameBucket) {
        try {
            boolean found = verifyBucketExists(nameBucket);
            if (found) {
                minioClient.removeBucket(RemoveBucketArgs.builder()
                        .bucket(nameBucket)
                        .build());
                return "Bucket " + nameBucket + " is removed successfully";
            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
            throw new RuntimeException(e.getMessage());
        }
        return "Bucket " + nameBucket + " does not exist";
    }

    public String create(String nameBucket) {
        try {
            boolean found = verifyBucketExists(nameBucket);
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(nameBucket)
                        .build());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return "Bucket " + nameBucket + " created successfully";
    }

    private boolean verifyBucketExists(String nameBucket) {
        boolean exists = false;
        try {
            exists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(nameBucket)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exists;
    }
}
