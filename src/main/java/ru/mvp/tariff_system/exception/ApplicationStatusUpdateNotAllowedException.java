package ru.mvp.tariff_system.exception;

public class ApplicationStatusUpdateNotAllowedException extends RuntimeException {

    public ApplicationStatusUpdateNotAllowedException(String message) {
        super(message);
    }
}
