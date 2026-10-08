package lv.nixx.poc.rest.model.payment;

import java.math.BigDecimal;

public record CreatePaymentRequest(BigDecimal amount) {
}