package com.sky.utils;

import com.sky.config.MinioConfig;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class MinioUtils {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioConfig minioConfig;

    /**
     * 文件上传
     *
     * @param bytes     文件字节数组
     * @param objectName 存储在MinIO中的对象名称
     * @return 文件访问URL
     */
    public String upload(byte[] bytes, String objectName) {
        try {
            // 上传文件
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfig.getBucket())
                            .object(objectName)
                            .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                            .build()
            );
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传到MinIO失败", e);
        }

        // 构建文件访问URL
        StringBuilder stringBuilder = new StringBuilder();
        // 处理endpoint是否包含http://或https://
        String endpoint = minioConfig.getEndpoint();
        if (endpoint.startsWith("http://") || endpoint.startsWith("https://")) {
            stringBuilder.append(endpoint).append("/");
        } else {
            stringBuilder.append("https://").append(endpoint).append("/");
        }
        stringBuilder.append(minioConfig.getBucket()).append("/").append(objectName);

        log.info("文件上传到:{}", stringBuilder.toString());
        return stringBuilder.toString();
    }

    /**
     * 处理MultipartFile上传，自动生成唯一文件名
     *
     * @param file 上传的文件
     * @return 文件访问URL
     */
    public String upload(MultipartFile file) {
        try {
            // 获取原始文件名
            String originalFilename = file.getOriginalFilename();
            // 获取文件后缀
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 生成唯一文件名：UUID + 日期目录 + 后缀
            String dateDir = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            String fileName = UUID.randomUUID().toString() + suffix;
            String objectName = dateDir + "/" + fileName;

            // 调用字节数组上传方法
            return upload(file.getBytes(), objectName);
        } catch (IOException e) {
            log.error("获取文件字节数组失败", e);
            throw new RuntimeException("获取文件内容失败", e);
        }
    }
}
