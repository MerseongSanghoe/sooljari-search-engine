package com.merseongsanghoe.sooljarisearchengine.document;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@Document(indexName = "auto-completion", createIndex = false)
@Setting(settingPath = "/AutoCompletionDocumentSettings.json", replicas = 0)
public class AutoCompletionDocument {
    @Id
    private String id;

    @Field(type = FieldType.Text,
//            store = true,
            analyzer = "completion_index_analyzer",
            searchAnalyzer = "completion_search_analyzer")
    private String keyword;

    private AutoCompletionDocument(String keyword) {
        this.keyword = keyword;
    }

    public static AutoCompletionDocument from(String keyword) {
        return new AutoCompletionDocument(keyword);
    }
}
