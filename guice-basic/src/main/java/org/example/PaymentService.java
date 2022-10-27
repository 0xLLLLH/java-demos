package org.example;

import com.google.inject.Inject;

public class PaymentService implements AbstractPaymentService {
    private final AbstractLoggerService loggerService;

    @Inject
    PaymentService(AbstractLoggerService loggerService) {
        this.loggerService = loggerService;
    }

    @Override
    public Invoice pay(String[] items, PaymentMethod method) {
        loggerService.log("PaymentService", "Processing payment...");
        return Invoice.createInvoice(items, method.name());
    }
}
