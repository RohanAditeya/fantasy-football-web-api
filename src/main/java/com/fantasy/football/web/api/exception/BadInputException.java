package com.fantasy.football.web.api.exception;

import lombok.Getter;

@Getter
public class BadInputException extends RuntimeException {

    private final int status;

    public BadInputException(String message, int status) {
        super(message);
        this.status = status;
    }
}