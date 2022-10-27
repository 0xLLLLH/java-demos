package org.example;

public interface AbstractPaymentService {
    Invoice pay(String[] items, PaymentMethod method);
}
