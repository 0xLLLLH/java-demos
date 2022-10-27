package org.example;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {
    static class StoreModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(AbstractPaymentService.class).to(PaymentService.class);
            bind(AbstractLoggerService.class).to(LoggerService.class);
        }
    }

    public static void main(String[] args) {
        Injector injector =  Guice.createInjector(new StoreModule());
        BookStore bookStore = injector.getInstance(BookStore.class);

        String[] items = new String[]{"book1", "book2"};

        bookStore.addItemsToCart(items);

        Invoice invoice = bookStore.purchase(PaymentMethod.Alipay);

        System.out.println(invoice.toString());
    }

    public static void originMain() {
        LoggerService realLogger = new LoggerService();
        PaymentService realPaymentService = new PaymentService(realLogger);
        BookStore bookStore = new BookStore(realPaymentService, realLogger);

        String[] items = new String[]{"book1", "book2"};

        bookStore.addItemsToCart(items);

        Invoice invoice = bookStore.purchase(PaymentMethod.Alipay);

        System.out.println(invoice.toString());
    }
}