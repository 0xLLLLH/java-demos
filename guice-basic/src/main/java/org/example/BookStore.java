package org.example;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Arrays;

public class BookStore {
    private final AbstractPaymentService paymentService;
    private final AbstractLoggerService loggerService;
    private final ArrayList<String> cart = new ArrayList<>();

    @Inject
    BookStore(AbstractPaymentService paymentService, AbstractLoggerService loggerService) {
        this.paymentService = paymentService;
        this.loggerService = loggerService;
    }

    public Invoice purchase(PaymentMethod paymentMethod) {
        loggerService.log("BookStore", "Request purchase");
        return this.paymentService.pay(cart.toArray(String[]::new), paymentMethod);
    }

    public void addItemsToCart(String[] items) {
        cart.addAll(Arrays.asList(items));
        loggerService.log("BookStore","cart updated");
    }
}
