package com.cryptolisting.dto;

import java.time.OffsetDateTime;
import java.util.List;

public class ApiError {

    private final OffsetDateTime timestamp;
    private final String message;
    private final List<String> details;

    public ApiError(String message, List<String> details) {
        this.timestamp = OffsetDateTime.now();
        this.message = message;
        this.details = details;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getDetails() {
        return details;
    }
}
