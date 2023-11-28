package com.merseongsanghoe.sooljarisearchengine.exception;

public class InconsistentDatabasesException extends RuntimeException {
    private final String indexName;

    public String getIndexName() {
        return this.indexName;
    }

    public InconsistentDatabasesException(String indexName) {
        super("두 데이터베이스의 동기화가 필요합니다.");

        this.indexName = indexName;
    }
}
