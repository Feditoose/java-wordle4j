package ru.yandex.practicum;

public class OutOfMaxStepsException extends RuntimeException {
    public OutOfMaxStepsException(String message) {
        super(message);
    }
}
