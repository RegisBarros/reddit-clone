package com.redditclone.exceptions;

public class RedditException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 8582870675929428869L;

    public RedditException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public RedditException(String exMessage) {
        super(exMessage);
    }
}
