package com.merseongsanghoe.sooljarisearchengine.DTO;

import lombok.Getter;

@Getter
public class ExceptionResponse {
    private String errorMessage;

    private ExceptionResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static ExceptionResponse of(Exception e) {
        return new ExceptionResponse(e.getMessage());
    }
}
