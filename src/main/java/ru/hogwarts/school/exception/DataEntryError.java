package ru.hogwarts.school.exception;

public class DataEntryError extends RuntimeException {
    public DataEntryError(String message) {
        super(message);
    }
}
