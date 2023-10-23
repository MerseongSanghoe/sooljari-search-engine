package com.merseongsanghoe.sooljarisearchengine.exception;

public class AlcoholDocumentNotFoundException extends RuntimeException {
    public AlcoholDocumentNotFoundException(Long id) {
        super("Alcohol id " + id.toString() + " 데이터를 인덱스에서 찾을 수 없습니다.");
    }
}
