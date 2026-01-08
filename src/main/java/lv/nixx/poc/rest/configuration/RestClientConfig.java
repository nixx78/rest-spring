package lv.nixx.poc.rest.configuration;

import lv.nixx.poc.rest.client.DeclarativeUserClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Bean
    public WebClient apiWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080/rest-spring/api")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Bean
    public DeclarativeUserClient declarativeUserClient() {

        WebClient webClient = WebClient.builder()
                .build();

        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(
                        WebClientAdapter.create(webClient)
                ).build();

        return factory.createClient(DeclarativeUserClient.class);
    }
}

