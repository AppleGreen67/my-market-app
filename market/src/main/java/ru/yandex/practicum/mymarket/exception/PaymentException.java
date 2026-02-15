package ru.yandex.practicum.mymarket.exception;

public class PaymentException extends Exception {
    private final String errorMessage;

    public PaymentException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
