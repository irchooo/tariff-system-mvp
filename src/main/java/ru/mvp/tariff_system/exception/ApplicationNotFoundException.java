package ru.mvp.tariff_system.exception;

public class ApplicationNotFoundException extends RuntimeException {

    public ApplicationNotFoundException(String message) {
        super(message);
    }
}
