package com.fantasy.football.web.api.exception;

import lombok.Getter;

@Getter
public class RecordNotFoundException extends RuntimeException {

    private final int status;

    public RecordNotFoundException (String message, int status) {
        super(message);
        this.status = status;
    }
}