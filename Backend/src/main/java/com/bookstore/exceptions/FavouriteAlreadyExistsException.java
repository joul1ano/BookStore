package com.bookstore.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class FavouriteAlreadyExistsException extends RuntimeException {
    public FavouriteAlreadyExistsException(String message) {
        super(message);
    }
}
