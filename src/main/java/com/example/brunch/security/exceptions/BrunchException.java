package com.example.brunch.security.exceptions;

import org.springframework.http.HttpStatus;

public class BrunchException extends RuntimeException {

    public enum Type {
        AUTHENTICATION,
        AUTHORIZATION,
        BAD_REQUEST,
        ENTITY_NOT_FOUND,
        ACCOUNT_EXISTS,
        NOT_FOUND,
        SERVER_ERROR
    }

    private final Type type;
    private final String description;
    private final Throwable throwable;

    public BrunchException(final Type type) {
        super(type.name());
        this.type = type;
        description = null;
        throwable = null;
    }

    public BrunchException(final Type type, final String description) {
        super(type.name() + " : " + description);
        this.type = type;
        this.description = description;
        throwable = null;
    }

    public BrunchException(final Type type, final String description, final Throwable throwable) {
        super(type.name() + " : " + description);
        this.type = type;
        this.description = description;
        this.throwable = throwable;
    }

    public HttpStatus getStatusCode() {
        return switch (type) {
            case AUTHENTICATION -> HttpStatus.UNAUTHORIZED;
            case AUTHORIZATION -> HttpStatus.FORBIDDEN;
            case BAD_REQUEST -> HttpStatus.BAD_REQUEST;
            case ENTITY_NOT_FOUND -> HttpStatus.UNPROCESSABLE_ENTITY;
            case ACCOUNT_EXISTS -> HttpStatus.NOT_ACCEPTABLE;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case SERVER_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    public Type getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public String toString() {
        return "BcApiException{" +
                "type=" + type +
                "description=" + description +
                '}';
    }
}
