package ru.mvp.tariff_system.exception;

public class TariffAlreadyExistsException extends RuntimeException {

    public TariffAlreadyExistsException(String message) {
        super(message);
    }
}
