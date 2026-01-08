package lv.nixx.poc.rest.controller;

import lv.nixx.poc.rest.configuration.ApiVersions;
import lv.nixx.poc.rest.model.UserDtoV1;
import lv.nixx.poc.rest.model.UserDtoV2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping(version = ApiVersions.V1)
    public List<UserDtoV1> getUsersV1() {
        return List.of(
                new UserDtoV1(1L, "John"),
                new UserDtoV1(2L, "Mery")
        );
    }

    @GetMapping(version = ApiVersions.V2)
    public List<UserDtoV2> getUsersV2() {
        return List.of(
                new UserDtoV2(1L, "John", "john@mail.me"),
                new UserDtoV2(2L, "Mery", ",mery@mail.me")
        );
    }
}
