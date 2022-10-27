package org.example;

public class LoggerService implements AbstractLoggerService {

    @Override
    public void log(String scope, String message) {
        System.out.format("[Logger] %s: %s\n", scope, message);
    }
}
