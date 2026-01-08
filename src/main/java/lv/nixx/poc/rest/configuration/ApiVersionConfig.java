package lv.nixx.poc.rest.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApiVersionConfig {

    @Bean
    public WebMvcConfigurer apiVersioningConfigurer() {

        return new WebMvcConfigurer() {
            @Override
            public void configureApiVersioning(ApiVersionConfigurer configurer) {
                configurer
                        .useRequestHeader(ApiVersions.X_API_VERSION)
                        .setDefaultVersion(ApiVersions.V1);
            }

        };
    }

}
