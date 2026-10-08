package lv.nixx.poc.rest.controller;

import lv.nixx.poc.rest.model.payment.CreatePaymentRequest;
import lv.nixx.poc.rest.model.payment.PaymentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final Map<String, PaymentResponse> payments = new ConcurrentHashMap<>();

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody CreatePaymentRequest request) {

        var response = payments.computeIfAbsent(
                idempotencyKey,
                key -> {
                    System.out.println("Creating NEW payment");
                    return new PaymentResponse(
                            UUID.randomUUID().toString(),
                            request.amount()
                    );
                }
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
