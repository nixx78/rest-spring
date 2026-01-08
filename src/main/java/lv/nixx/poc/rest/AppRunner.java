package lv.nixx.poc.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.Arrays;

@SpringBootApplication
public class AppRunner extends SpringBootServletInitializer {

    private static final Logger log = LoggerFactory.getLogger(AppRunner.class);

    public static void main(String[] args) {

        log.info("Application parameters in main class: {}", Arrays.toString(args));

        SpringApplication.run(AppRunner.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        // This configuration will work only in case of we run our application on Tomcat server
        return application.sources(AppRunner.class)
                .profiles("tomcat");
    }

}