package com.korit.silverbutton.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${root.path}")
    private String rootPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ensure rootPath ends with a slash for consistency
        if (!rootPath.endsWith("/")) {
            rootPath += "/";
        }

        registry.addResourceHandler("/file/**")
                .addResourceLocations("file:///" + rootPath)
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        System.out.println("Decoded resourcePath: " + resourcePath);
                        System.out.println("Resolved location: " + location.getURI());
                        return super.getResource(resourcePath, location);
                    }
                });
    }
}
