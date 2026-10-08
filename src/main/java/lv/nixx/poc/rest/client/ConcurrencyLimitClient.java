package lv.nixx.poc.rest.client;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ConcurrencyLimitClient {

    public static void main(String[] args) {

        WebClient client = WebClient.builder()
                .baseUrl("http://localhost:8080/rest-spring")
                .build();

        int parallelRequests = 5;
        Flux.range(1, parallelRequests)
                .flatMap(i ->
                        client.get()
                                .uri("/api/users/exceedRateLimit")
                                .retrieve()
                                .bodyToMono(String.class)
                                .timeout(Duration.ofSeconds(5))
                                .doOnSubscribe(s ->
                                        System.out.println("→ Request " + i + " sent"))
                                .doOnSuccess(r ->
                                        System.out.println("✔ Request " + i + " OK"))
                                .doOnError(e ->
                                        System.out.println("❌ Request " + i + " ERROR: " + e.getMessage()))
                )
                .blockLast();
    }

}
