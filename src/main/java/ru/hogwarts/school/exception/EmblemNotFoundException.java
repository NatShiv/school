package ru.hogwarts.school.exception;

public class EmblemNotFoundException extends RuntimeException {
    private final long id;

    public EmblemNotFoundException(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
