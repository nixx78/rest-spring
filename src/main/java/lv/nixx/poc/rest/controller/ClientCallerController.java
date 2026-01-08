package lv.nixx.poc.rest.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lv.nixx.poc.rest.client.DeclarativeUserClient;
import lv.nixx.poc.rest.client.ReactiveUserClient;
import lv.nixx.poc.rest.configuration.ApiVersions;
import lv.nixx.poc.rest.model.UserDtoV1;
import lv.nixx.poc.rest.model.UserDtoV2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collection;

@RestController
@RequestMapping("client/rest")
public class ClientCallerController {

    private final ReactiveUserClient reactiveUserClient;
    private final DeclarativeUserClient declarativeUserClient;

    public ClientCallerController(ReactiveUserClient reactiveUserClient,
                                  DeclarativeUserClient declarativeUserClient) {
        this.reactiveUserClient = reactiveUserClient;
        this.declarativeUserClient = declarativeUserClient;
    }

    @GetMapping("/users/v1")
    public Mono<Collection<UserDtoV1>> callVersion1() {
        return reactiveUserClient.getUsersV1();
    }

    @GetMapping("/users/v2")
    public Mono<Collection<UserDtoV2>> callVersion2() {
        return reactiveUserClient.getUsersV2();
    }

    @GetMapping("/users/declarative")
    public Collection<?> getUsersByVersion(
            @Parameter(
                    description = "API version",
                    required = true,
                    schema = @Schema(
                            type = "string",
                            allowableValues = {
                                    ApiVersions.V1,
                                    ApiVersions.V2
                            },
                            example = ApiVersions.V1
                    )
            )
            @RequestParam String version) {
        return declarativeUserClient.getUsersByVersion(version);
    }

}
