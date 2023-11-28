package com.merseongsanghoe.sooljarisearchengine.exception;

public class AlcoholNodeNotFoundException extends RuntimeException {
    public AlcoholNodeNotFoundException(Long id) {
        super("Alcohol id " + id.toString() + " 데이터를 Neo4j에서 찾을 수 없습니다.");
    }
}
