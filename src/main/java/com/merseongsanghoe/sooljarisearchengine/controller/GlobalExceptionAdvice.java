package com.merseongsanghoe.sooljarisearchengine.controller;

import com.merseongsanghoe.sooljarisearchengine.DTO.ExceptionResponse;
import com.merseongsanghoe.sooljarisearchengine.exception.AlcoholDocumentNotFoundException;
import com.merseongsanghoe.sooljarisearchengine.exception.AlcoholNotFoundException;
import com.merseongsanghoe.sooljarisearchengine.exception.CompletionKeywordDuplicatedException;
import com.merseongsanghoe.sooljarisearchengine.exception.RequiredRequestBodyIsMissingException;
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

    @ExceptionHandler({
            RequiredRequestBodyIsMissingException.class,
    })
    public ResponseEntity<ExceptionResponse> badRequestException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.of(e));
    }

    @ExceptionHandler({
            CompletionKeywordDuplicatedException.class,
    })
    public ResponseEntity<ExceptionResponse> conflictException(Exception e){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ExceptionResponse.of(e));
    }
}
