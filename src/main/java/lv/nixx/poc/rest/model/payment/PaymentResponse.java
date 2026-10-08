package lv.nixx.poc.rest.model.payment;

import java.math.BigDecimal;

public record PaymentResponse(
        String paymentId,
        BigDecimal amount
) {
}