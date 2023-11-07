package com.merseongsanghoe.sooljarisearchengine.exception;

public class RequiredRequestBodyIsMissingException extends RuntimeException {
    public RequiredRequestBodyIsMissingException(String requiredRequestBody) {
        super("Request Body '" + requiredRequestBody + "' 값은 필수입니다.");
    }
}
