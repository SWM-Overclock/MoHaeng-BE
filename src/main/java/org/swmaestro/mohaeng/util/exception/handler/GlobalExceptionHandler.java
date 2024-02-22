package org.swmaestro.mohaeng.util.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.swmaestro.mohaeng.util.exception.InvalidRefreshTokenException;
import org.swmaestro.mohaeng.util.exception.NotExpiredTokenException;
import org.swmaestro.mohaeng.util.exception.RefreshTokenMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotExpiredTokenException.class)
    public ResponseEntity<?> handleNotExpiredTokenException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(RefreshTokenMismatchException.class)
    public ResponseEntity<?> handleRefreshTokenMismatchException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<?> handleInvalidRefreshTokenException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}