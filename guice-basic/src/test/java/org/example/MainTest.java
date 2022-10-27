package org.example;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertEquals;

public class MainTest {
    static class FakeLoggerService implements AbstractLoggerService {
        @Override
        public void log(String scope, String message) {
            System.out.format("[Fake Logger] %s: %s\n", scope, message);
        }
    }

    static class MockStoreModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(AbstractPaymentService.class).to(PaymentService.class);
            bind(AbstractLoggerService.class).to(FakeLoggerService.class);
        }
    }

    public Injector injector = null;

    @Before
    public void setUp() {
        this.injector = Guice.createInjector(new MockStoreModule());
    }

    @Test
    public void main() {
        BookStore bookStore = injector.getInstance(BookStore.class);

        String[] items = new String[]{"book1", "book2"};

        bookStore.addItemsToCart(items);

        Invoice invoice = bookStore.purchase(PaymentMethod.Alipay);

        assertEquals(invoice.toString(), "Invoice\nBuy: book1,book2\nPay with: Alipay");
    }
}