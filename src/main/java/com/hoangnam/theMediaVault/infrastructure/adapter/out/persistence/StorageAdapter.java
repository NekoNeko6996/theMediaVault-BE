package com.hoangnam.theMediaVault.infrastructure.adapter.out.persistence;

import com.hoangnam.theMediaVault.application.port.out.StoragePort;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StorageAdapter implements StoragePort {

    private final MinioClient minioClient;
    @Value("${minio.bucket}")
    private String bucketName;
    @Value("${minio.download.url.expiration}")
    private int downloadExpiration;

    @Override
    public String upload(String path, InputStream inputStream, long size, String contentType) {
        try {
            return minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build()
            ).etag();
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidResponseException | ServerException | XmlParserException | IOException | IllegalArgumentException | InvalidKeyException | NoSuchAlgorithmException e) {
            // Bọc checked exception thành unchecked exception
            throw new RuntimeException("MinIO upload failed for path: " + path, e);
        }
    }

    @Override
    public void delete(String path) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error durring delete your file for path: " + path, e);
        }
    }

    @Override
    public boolean exists(String path) {
        try {
            minioClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(path).build());
            return true;
        } catch (ErrorResponseException e) {
            if ("NoSuchKey".equals(e.errorResponse().code())
                    || "NoSuchObject".equals(e.errorResponse().code())) {
                return false;
            }
            throw new RuntimeException("Error occurred while checking object existence", e);
        } catch (Exception e) {
            // Handle other potential SDK or network exceptions
            throw new RuntimeException("Unexpected error connecting to MinIO", e);
        }
    }

    @Override
    public String getDownloadUrl(String path, String type) {
        try {
            Map<String, String> reqParams = new HashMap<>();
            type = type.equals("preview") ? "inline" : "attachment";
            reqParams.put("response-content-disposition", type);
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(path)
                            .extraQueryParams(reqParams)
                            .expiry(downloadExpiration, TimeUnit.MINUTES)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error durring create dowload url for: " + path, e);
        }
    }

    @Override
    public void rename(String newPath, String oldPath) {
        try {
            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .bucket(bucketName)
                            .object(newPath)
                            .source(
                                    CopySource.builder()
                                            .bucket(bucketName)
                                            .object(oldPath)
                                            .build()
                            )
                            .build()
            );

            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(oldPath)
                    .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error durring rename your file for path: " + oldPath, e);
        }
    }

}
