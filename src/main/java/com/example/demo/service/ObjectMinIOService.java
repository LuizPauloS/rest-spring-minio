package com.example.demo.service;

import com.example.demo.model.ArquivosMinIO;
import io.minio.*;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ObjectMinIOService {

    @Autowired
    private MinioClient minioClient;

    public void uploadFile(MultipartFile files, String nameBucket) {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(nameBucket).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(nameBucket).build());
            }
            minioClient.putObject(PutObjectArgs.builder()
                            .bucket(nameBucket)
                            .object(files.getOriginalFilename())
                            .stream(files.getInputStream(), files.getBytes().length, -1)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public byte[] downloadFile(String nameFile, String nameBucket) throws IOException {
        InputStream stream = null;
        try {
            stream = minioClient.getObject(GetObjectArgs.builder()
                                    .bucket(nameBucket)
                                    .object(nameFile)
                                    .build());
            byte[] bytes = stream.readAllBytes();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(stream != null) stream.close();
        }
        return null;
    }

    public List<ArquivosMinIO> listObjects(String bucketName) {
        List<ArquivosMinIO> arquivosMinIOS = new ArrayList<>();
        try {
            Iterable<Result<Item>> results =
                    minioClient.listObjects(ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .build());
            for (Result<Item> result : results) {
                Item item = result.get();
               arquivosMinIOS.add(new ArquivosMinIO(item.objectName(), item.lastModified().toString(), item.size()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arquivosMinIOS;
    }

    public String delete(String bucketName, String nameObject) {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());
            if (found) {
                minioClient.removeObject(RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(nameObject)
                        .build());
                return "Object " + nameObject + " is removed successfully";
            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
            throw new RuntimeException(e.getMessage());
        }
        return "Object " + nameObject + " does not exist";
    }
}
