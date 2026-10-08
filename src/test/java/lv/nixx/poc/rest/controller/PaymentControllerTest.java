package lv.nixx.poc.rest.controller;

import lv.nixx.poc.rest.model.payment.CreatePaymentRequest;
import lv.nixx.poc.rest.model.payment.PaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentControllerTest {

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port + "/rest-spring")
                .build();
    }

    @Test
    void shouldReturnSamePaymentForSameIdempotencyKey() {

        var request = new CreatePaymentRequest(
                new BigDecimal("100.00")
        );

        String idempotencyKey = "test-key-123";

        var firstResponse = restClient.post()
                .uri("/payments")
                .header("Idempotency-Key", idempotencyKey)
                .body(request)
                .retrieve()
                .body(PaymentResponse.class);

        var secondResponse = restClient.post()
                .uri("/payments")
                .header("Idempotency-Key", idempotencyKey)
                .body(request)
                .retrieve()
                .body(PaymentResponse.class);

        assertAll(
                () -> assertThat(firstResponse).isNotNull(),
                () -> assertThat(secondResponse).isNotNull(),
                () -> assertThat(secondResponse.paymentId()).isEqualTo(firstResponse.paymentId()),
                () -> assertThat(secondResponse.amount()).isEqualByComparingTo(firstResponse.amount())
        );

    }
}
