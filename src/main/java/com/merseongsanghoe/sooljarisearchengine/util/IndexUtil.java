package com.merseongsanghoe.sooljarisearchengine.util;

public class IndexUtil {
    /**
     * {indexName}-{timestamp} 형식의 인덱스 이름 생성
     * @param indexName
     * @return "{indexName}-{timestamp}"
     */
    public static String getIndexNameWithTimestamp(String indexName) {
        return indexName + "-" + System.currentTimeMillis();
    }
}
