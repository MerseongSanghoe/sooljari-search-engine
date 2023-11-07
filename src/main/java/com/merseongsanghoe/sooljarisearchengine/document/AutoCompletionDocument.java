package com.merseongsanghoe.sooljarisearchengine.document;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@Document(indexName = "auto-completion", createIndex = false)
@Setting(settingPath = "/AutoCompletionDocumentSettings.json")
public class AutoCompletionDocument {
    @Id
    private String id;

    @MultiField(
            mainField = @Field(
                    type = FieldType.Text,
                    analyzer = "completion_index_analyzer",
                    searchAnalyzer = "completion_search_analyzer"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword)
            }
    )
    private String keyword;

    private AutoCompletionDocument(String keyword) {
        this.keyword = keyword;
    }

    public static AutoCompletionDocument from(String keyword) {
        return new AutoCompletionDocument(keyword);
    }
}
