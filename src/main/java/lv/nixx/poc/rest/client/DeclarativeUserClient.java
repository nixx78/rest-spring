package lv.nixx.poc.rest.client;

import lv.nixx.poc.rest.configuration.ApiVersions;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.Collection;

@HttpExchange(value = "http://localhost:8080/rest-spring/api")
public interface DeclarativeUserClient {

    @GetExchange("/users")
    Collection<?> getUsersByVersion(@RequestHeader(ApiVersions.X_API_VERSION) String version);
}

