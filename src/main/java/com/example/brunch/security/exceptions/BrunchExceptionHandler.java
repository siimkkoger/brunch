package com.example.brunch.security.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Date;

@ControllerAdvice
public class BrunchExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BrunchException.class)
    public ResponseEntity<Object> handleBrunchException(final BrunchException exception, final WebRequest request) {
        final HttpHeaders headers = new HttpHeaders();
        final ErrorMessage errorMessage = buildErrorMessageBody(exception, request);
        return handleExceptionInternal(exception, errorMessage, headers, exception.getStatusCode(), request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) {
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
        return buildResponseEntity(new BrunchException(BrunchException.Type.SERVER_ERROR, ex.getMessage()), request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>("Something...", status);
    }

    private ErrorMessage buildErrorMessageBody(final BrunchException exception, final WebRequest request) {
        return new ErrorMessage(
                new Date(),
                exception.getStatusCode().value(),
                exception.getDescription(),
                request.getDescription(false));
    }

    private ResponseEntity<ErrorMessage> buildResponseEntity(final BrunchException exception, final WebRequest request) {
        final ErrorMessage message = new ErrorMessage(
                new Date(),
                exception.getStatusCode().value(),
                exception.getDescription(),
                request.getDescription(false));
        return new ResponseEntity<>(message, exception.getStatusCode());
    }

}
