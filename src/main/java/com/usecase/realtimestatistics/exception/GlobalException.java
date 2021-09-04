package com.usecase.realtimestatistics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class GlobalException extends ResponseStatusException {
    public GlobalException(HttpStatus status) {
        super(status);
    }

    public GlobalException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
