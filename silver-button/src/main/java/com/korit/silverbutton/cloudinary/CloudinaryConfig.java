package com.korit.silverbutton.cloudinary;
import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dp0m0k4cv");  // Cloudinary에서 제공된 Cloud Name
        config.put("api_key", "962128558985342"); // Cloudinary에서 제공된 API Key
        config.put("api_secret", "D8aX3gNs-02z63NVIldD71-sEg0"); // Cloudinary에서 제공된 API Secret

        return new Cloudinary(config);
    }
}