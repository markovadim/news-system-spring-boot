package ru.clevertec.loggerstarter.exceptions;

public class NewsNotFoundException extends RuntimeException {

    public NewsNotFoundException(long id) {
        super(String.format("News with id: %d not found.", id));
    }
}
