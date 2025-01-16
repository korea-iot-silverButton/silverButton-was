package com.korit.silverbutton.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class GcpStorageConfig {
    @Bean  // Storage 객체를 빈으로 등록합니다.
    public Storage storage() throws IOException {
        // ClassPathResource 경로에서 "classpath:"는 필요하지 않음
        ClassPathResource resource = new ClassPathResource("plucky-command-448000-h4-a9767699a3b3.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
        String projectId = "a9767699a3b3028dfbc58fbbe06c950225117687";
        return StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build()
                .getService();
    }
}
