package ru.mvp.tariff_system.exception;

public class ApplicationPaymentNotAllowedException extends RuntimeException {

    public ApplicationPaymentNotAllowedException(String message) {
        super(message);
    }
}
