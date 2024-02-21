package org.swmaestro.mohaeng.util.exception;

public class NotExpiredTokenException extends RuntimeException {

    public NotExpiredTokenException() {
        super();
    }

    public NotExpiredTokenException(String message) {
        super(message);
    }
}
