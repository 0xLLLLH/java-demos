# Guice Basic

## 心智模型

Guice可以简化理解为一个Map，Guice Map的key就是`Key`，value就是`Provider`，而Map的每个`key-value`键值对（Entry）就是一个`Binding`。

以下是一些DSL对应的Guice Map心智模型：

| Guice DSL syntax |                              Mental model                              |
|:---:|:----------------------------------------------------------------------:|
|`bind(key).toInstance(value)`|             `map.put(key, () -> value)`(instance binding)              |
|`bind(key).toProvider(provider)`|              `map.put(key, provider)` (provider binding)               |
|`bind(key).to(anotherKey)`|          `map.put(key, map.get(anotherKey))`(linked binding)           |
|`@Provides Foo provideFoo() {...}`|`map.put(Key.get(Foo.class), module::provideFoo)`(provider method binding)|

## Key和 Provider的一些细节

### Key
Guice key是Guice Map中索引和获取Provider的一个值，用于区分依赖。

每个被声明的依赖都会创建对应的Key，例如在构造器中声明的依赖：
```java
class BookStore {
    @Inject
    BookStore(AbstractPaymentService paymentService, AbstractLoggerService loggerService) {
        //...
    }
}
```
会使用Key类的静态方法创建，对应的Key对象:
```
Key<AbstractPaymentService> key1 = Key.get(AbstractPaymentService.class);
Key<AbstractLoggerService> key2 = Key.get(AbstractLoggerService.class);
```

后续进行注入时，便用这个Key到Guice map中查找Provider。

需要注意的是，如果依赖具有相同的类型而又想对其进行区分，可以为其加上[注解](https://github.com/google/guice/wiki/BindingAnnotations)。
例如[官方文档](https://github.com/google/guice/wiki/MentalModel#guice-keys)中便展示了区分两个String类型依赖的写法，具体注解定义方法参考[文档](https://github.com/google/guice/wiki/BindingAnnotations)(或者也可以不每次定义新注解，直接用[内置的注解](https://github.com/google/guice/wiki/BindingAnnotations#named)`@Named("GivenName")`)。
```java
final class MultilingualGreeter {
  private String englishGreeting;
  private String spanishGreeting;

  @Inject
  MultilingualGreeter(
      @English String englishGreeting, @Spanish String spanishGreeting) {
    this.englishGreeting = englishGreeting;
    this.spanishGreeting = spanishGreeting;
  }
}
```
注解相当于多用一个参数来创建、查询Key：`Key<String> key = Key.get(String.class, English.class);`，定义provider时也需要加上对应的annotation。

### Provider
Provider是Guice Map的值，用于创建应用中用到的的对象。

一般Provider并不由用户手动创建，而是Guice根据Module来创建：
```java
static class StoreModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AbstractPaymentService.class).to(PaymentService.class);
    }

    @Provides
    protected AbstractLoggerService provideLoggerService(LoggerService loggerService) {
        return loggerService;
    }
}
```

上面的代码会创建两个Provider，二者都是用派生类代替抽象类实例化，但略有不同：
* `Provider<AbstractPaymentService>`使用`PaymentService`代替`AbstractPaymentService`进行实例化
* `Provider<AbstractLoggerService>`使用`provideLoggerService()`的返回值`loggerService`，而`loggerService`的值由Guice实例化`LoggerService`并注入

## Bindings
Guice支持[多种Bindings类型](https://github.com/google/guice/wiki/Bindings)，现阶段时间有限了解Linked Bindings，其他的后续有需要再去研究。

### Linked Bindings

Linked Bindings主要用于将一个接口与其实现绑定，这个绑定可以是链式的，例如：

```java
public class BillingModule extends AbstractModule {
    @Provides
    TransactionLog provideTransactionLog(DatabaseTransactionLog databaseTransactionLog) {
        return databaseTransactionLog;
    }

    @Provides
    DatabaseTransactionLog provideDatabaseTransactionLog(MySqlDatabaseTransactionLog impl) {
        return impl;
    }
}
```

