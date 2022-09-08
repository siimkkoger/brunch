package com.example.brunch.security.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Date;

@ControllerAdvice
public class BrunchExceptionHandler {

    @ExceptionHandler(BrunchException.class)
    public ResponseEntity<Object> handleBrunchException(final BrunchException exception, final WebRequest request) {
        return buildResponseEntity(exception, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Constraint violations: [\n");
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            builder.append(violation.getPropertyPath()).append(" ").append(violation.getMessage()).append("\n");
        }
        builder.append("]");
        return buildResponseEntity(new BrunchException(BrunchException.Type.BAD_REQUEST, builder.toString()), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {
        final ErrorMessage message = new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> buildResponseEntity(final BrunchException exception, final WebRequest request) {
        final ErrorMessage message = new ErrorMessage(
                exception.getStatusCode().value(),
                new Date(),
                exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(message, exception.getStatusCode());
    }
}
