package lv.nixx.poc.rest.client;

import lv.nixx.poc.rest.configuration.ApiVersions;
import lv.nixx.poc.rest.model.UserDtoV1;
import lv.nixx.poc.rest.model.UserDtoV2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collection;

import static lv.nixx.poc.rest.configuration.ApiVersions.X_API_VERSION;

@Component
public class ReactiveUserClient {


    private final WebClient webClient;

    public ReactiveUserClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Collection<UserDtoV1>> getUsersV1() {
        return webClient.get()
                .uri("/users")
                .header(X_API_VERSION, ApiVersions.V1)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }

    public Mono<Collection<UserDtoV2>> getUsersV2() {
        return webClient.get()
                .uri("/users")
                .header(X_API_VERSION, ApiVersions.V2)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });

    }
}

