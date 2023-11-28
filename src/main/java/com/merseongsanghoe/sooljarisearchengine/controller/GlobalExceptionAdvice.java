package com.merseongsanghoe.sooljarisearchengine.controller;

import com.merseongsanghoe.sooljarisearchengine.DTO.ExceptionResponse;
import com.merseongsanghoe.sooljarisearchengine.exception.*;
import com.merseongsanghoe.sooljarisearchengine.service.IndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionAdvice {
    private final IndexService indexService;

    @ExceptionHandler({
            AlcoholNotFoundException.class,
            AlcoholDocumentNotFoundException.class,
            AlcoholNodeNotFoundException.class
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

    @ExceptionHandler({
            InconsistentDatabasesException.class,
    })
    public ResponseEntity<ExceptionResponse> inconsisntentDatabasesException(InconsistentDatabasesException e){
        // 제대로 인덱싱을 완료하지 못한 인덱스 삭제 요청
        // TODO: 여기서 Service 계층을 호출하는 것이 옳은 구조인가.... 고민 필요
        this.indexService.removeIndex(e.getIndexName());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ExceptionResponse.of(e));
    }
}
