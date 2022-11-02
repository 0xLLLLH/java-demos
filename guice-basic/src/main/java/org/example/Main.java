package org.example;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;

public class Main {
    static class StoreModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(AbstractPaymentService.class).to(PaymentService.class);
        }

        @Provides
        protected AbstractLoggerService provideLoggerService(LoggerService loggerService) {
            // loggerService由Guice实例化LoggerService并注入
            // 这种写法主要用于：
            // 1. 实例化依赖其他的模块
            // 2. 实例化时或实例化后需要进行一些配置
            return loggerService;
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