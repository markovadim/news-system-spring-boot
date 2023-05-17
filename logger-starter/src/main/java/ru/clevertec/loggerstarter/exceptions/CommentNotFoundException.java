package ru.clevertec.loggerstarter.exceptions;


public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException(long id) {
        super(String.format("Comment with id: %d not found.", id));
    }
}
