package com.redditclone.exceptions;

public class PostNotFoundException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = -2337913903290476036L;

    public PostNotFoundException(String message) {
        super(message);
    }
}
