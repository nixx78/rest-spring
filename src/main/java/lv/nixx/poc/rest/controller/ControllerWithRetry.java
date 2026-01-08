package lv.nixx.poc.rest.controller;

import lv.nixx.poc.rest.model.UserDtoV1;
import lv.nixx.poc.rest.service.ServiceWithRetry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class ControllerWithRetry {

    private final ServiceWithRetry service;

    public ControllerWithRetry(ServiceWithRetry service) {
        this.service = service;
    }

    @GetMapping("/retry/users")
    public Collection<UserDtoV1> getUsersWithRetry(ServiceWithRetry.Type type) {
        return service.loadUsers(type);
    }

}
