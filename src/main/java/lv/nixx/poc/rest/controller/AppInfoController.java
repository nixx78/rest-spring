package lv.nixx.poc.rest.controller;

import lv.nixx.poc.rest.service.AppInfoProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AppInfoController {

    private static final String NOT_SET = "NOT_SET";

    private final AppInfoProvider appInfoProvider;

    public AppInfoController(AppInfoProvider appInfoProvider) {
        this.appInfoProvider = appInfoProvider;
    }

    @GetMapping("/appInfo")
    public AppInfoProvider.AppInfo getVersion() {
        return appInfoProvider.getInfo();
    }

    @GetMapping("/variables")
    public Map<String, String> getVariables() {
        Map<String, String> env = System.getenv();
        return Map.of(
                "VARIABLE_FROM_DOCKERFILE", env.getOrDefault("VARIABLE_FROM_DOCKERFILE", NOT_SET),
                "SPRING_PROFILES_ACTIVE", env.getOrDefault("SPRING_PROFILES_ACTIVE", NOT_SET)
        );
    }

}
