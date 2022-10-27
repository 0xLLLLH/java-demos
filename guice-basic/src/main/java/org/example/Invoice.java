package org.example;

import com.google.common.base.Joiner;

public class Invoice {
    private final String[] items;
    private final String paymentMethodName;

    private Invoice(String[] items, String paymentMethodName) {
        this.items = items;
        this.paymentMethodName = paymentMethodName;
    }

    static Invoice createInvoice(String[] items, String paymentMethodName) {
        return new Invoice(items, paymentMethodName);
    }

    @Override
    public String toString() {
        return String.format("Invoice\nBuy: %s\nPay with: %s", Joiner.on(",").join(items), paymentMethodName);
    }

}
