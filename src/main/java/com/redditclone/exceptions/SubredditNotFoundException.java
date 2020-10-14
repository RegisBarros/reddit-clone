package com.redditclone.exceptions;

public class SubredditNotFoundException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 4015907687516854822L;

    public SubredditNotFoundException(String message) {
        super(message);
    }
}
