package com.merseongsanghoe.sooljarisearchengine.exception;

public class RequiredRequestParamIsMissingException extends RuntimeException {
    public RequiredRequestParamIsMissingException(String requiredRequestParam) {
        super("Request Param '" + requiredRequestParam + "' 값은 필수입니다.");
    }
}
