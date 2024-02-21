package org.swmaestro.mohaeng.util.exception;

public class RefreshTokenMismatchException extends RuntimeException {

    public RefreshTokenMismatchException() {
        super();
    }

    public RefreshTokenMismatchException(String message) {
        super(message);
    }
}
