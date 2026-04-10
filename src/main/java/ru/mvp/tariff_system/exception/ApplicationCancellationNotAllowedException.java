package ru.mvp.tariff_system.exception;

public class ApplicationCancellationNotAllowedException extends RuntimeException {

    public ApplicationCancellationNotAllowedException(String message) {
        super(message);
    }
}
