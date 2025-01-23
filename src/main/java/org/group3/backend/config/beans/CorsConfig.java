/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2025
 * @Time : 10:07 AM
 */
package org.group3.backend.config.beans;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CorsConfig {

    @Value("${application.frontend-urls}")
    private String frontendUrlsCsv;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@Nonnull CorsRegistry registry) {
                List<String> allowedFrontendUrls = parseFrontendUrls(frontendUrlsCsv);
                registry.addMapping("/**")
                        .allowedOriginPatterns(allowedFrontendUrls.toArray(new String[0]))
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("")
                        .allowCredentials(true);
            }
        };
    }

    private List<String> parseFrontendUrls(String csv) {
        return List.of(csv.split("\\s*,\\s*"));
    }
}