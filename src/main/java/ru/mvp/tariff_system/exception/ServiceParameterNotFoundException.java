package ru.mvp.tariff_system.exception;

public class ServiceParameterNotFoundException extends RuntimeException {

    public ServiceParameterNotFoundException(String message) {
        super(message);
    }
}
