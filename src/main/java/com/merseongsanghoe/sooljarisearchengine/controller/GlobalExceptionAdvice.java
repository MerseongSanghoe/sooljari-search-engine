package com.merseongsanghoe.sooljarisearchengine.controller;

import com.merseongsanghoe.sooljarisearchengine.DTO.ExceptionResponse;
import com.merseongsanghoe.sooljarisearchengine.exception.AlcoholDocumentNotFoundException;
import com.merseongsanghoe.sooljarisearchengine.exception.AlcoholNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler({
            AlcoholNotFoundException.class,
            AlcoholDocumentNotFoundException.class,
    })
    public ResponseEntity<ExceptionResponse> notFoundException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.of(e));
    }
}
