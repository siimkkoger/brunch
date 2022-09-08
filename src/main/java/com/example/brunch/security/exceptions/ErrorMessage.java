package com.example.brunch.security.exceptions;

import java.util.Date;

public record ErrorMessage(int statusCode, Date timestamp, String message, String description) {
}