package com.example.brunch.security.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Date;

@ControllerAdvice
public class BrunchExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BrunchException.class)
    public ResponseEntity<Object> handleBrunchException(final BrunchException exception, final WebRequest request) {
        final HttpHeaders headers = new HttpHeaders();
        final ErrorMessage errorMessage = buildErrorMessageBody(exception.getStatusCode(), exception.getDescription(), request);
        return handleExceptionInternal(exception, errorMessage, headers, exception.getStatusCode(), request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Constraint violations: [\n");
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            builder.append(violation.getPropertyPath()).append(" ").append(violation.getMessage()).append("\n");
        }
        builder.append("]");

        final HttpHeaders headers = new HttpHeaders();
        final BrunchException exception = new BrunchException(BrunchException.Type.BAD_REQUEST, builder.toString());
        final ErrorMessage errorMessage = buildErrorMessageBody(exception.getStatusCode(), exception.getDescription(), request);
        return handleExceptionInternal(exception, errorMessage, headers, exception.getStatusCode(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> globalExceptionHandler(Exception ex, WebRequest request) {
        final HttpHeaders headers = new HttpHeaders();
        final BrunchException exception = new BrunchException(BrunchException.Type.SERVER_ERROR, ex.getMessage());
        final ErrorMessage errorMessage = buildErrorMessageBody(exception.getStatusCode(), exception.getDescription(), request);
        return handleExceptionInternal(exception, errorMessage, headers, exception.getStatusCode(), request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final ErrorMessage body = buildErrorMessageBody(HttpStatus.NOT_FOUND, "Handler not found!", request);
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    private ErrorMessage buildErrorMessageBody(final HttpStatus httpStatus, final String errorMessage, final WebRequest request) {
        return new ErrorMessage(
                new Date(),
                httpStatus.value(),
                errorMessage,
                ((ServletWebRequest)request).getRequest().getRequestURI());
    }

}
