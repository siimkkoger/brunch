package com.example.brunch.security.exceptions;

import java.util.Date;

public record ErrorMessage(Date timestamp, int status, String error, String path) {
}