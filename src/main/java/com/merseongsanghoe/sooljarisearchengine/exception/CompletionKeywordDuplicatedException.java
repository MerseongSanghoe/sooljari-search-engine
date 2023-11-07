package com.merseongsanghoe.sooljarisearchengine.exception;

public class CompletionKeywordDuplicatedException extends RuntimeException {
    public CompletionKeywordDuplicatedException(String keyword) {
        super("자동완성 키워드 '" + keyword + "'는 이미 존재하는 키워드입니다.");
    }
}
