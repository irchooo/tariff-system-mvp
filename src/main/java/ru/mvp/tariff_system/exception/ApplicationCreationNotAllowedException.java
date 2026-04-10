package ru.mvp.tariff_system.exception;

public class ApplicationCreationNotAllowedException extends RuntimeException {

    public ApplicationCreationNotAllowedException(String message) {
        super(message);
    }
}
