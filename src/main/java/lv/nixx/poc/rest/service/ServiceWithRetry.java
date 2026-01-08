package lv.nixx.poc.rest.service;

import lv.nixx.poc.rest.model.UserDtoV1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ServiceWithRetry {

    private static final Logger log = LoggerFactory.getLogger(ServiceWithRetry.class);

    private final AtomicInteger retryCount = new AtomicInteger(0);

    @Retryable(
            retryFor = {
                    WebClientResponseException.class,
                    IllegalStateException.class
            },
            maxAttempts = 3,
            backoff = @Backoff(
                    delay = 500,
                    multiplier = 2.0
            )
    )
    public Collection<UserDtoV1> loadUsers(Type type) {

        log.info("LoadUsers, retry [{}]", retryCount.incrementAndGet());

        if (Type.NORMAL.equals(type)) {
            return List.of(
                new UserDtoV1(1L, "Name1"),
                new UserDtoV1(2L, "Name2"),
                new UserDtoV1(3L, "Name3")
            );
        }

        throw new IllegalStateException("Expected exception");
    }

    @Recover
    public List<UserDtoV1> recover(Exception ex) {
        log.warn("Retry exhausted", ex);

        retryCount.set(0);

        return List.of();
    }

    public enum Type {
        NORMAL, ERROR
    }

}
