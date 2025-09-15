package com.sky.config;


import io.minio.MinioClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "minio")
@Data
@Slf4j
public class MinioConfig {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucket;
    @Bean
    @ConditionalOnMissingBean
    public MinioClient minioClient(){
        log.info("创建minio文件上传工具对象");
       MinioClient minioClient= MinioClient.builder()
                                .endpoint(endpoint)
                                .credentials(accessKey, secretKey).build();
        System.out.println(minioClient);
       return minioClient;
    }
}
