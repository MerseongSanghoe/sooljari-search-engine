package com.merseongsanghoe.sooljarisearchengine.exception;

public class AlcoholNotFoundException extends RuntimeException {
    public AlcoholNotFoundException(Long id) {
        super("Alcohol id " + id + " 데이터를 데이터베이스에서 찾을 수 없습니다.");
    }
}
