package com.merseongsanghoe.sooljarisearchengine.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Document(indexName = "alcohols")
@Setting(replicas = 0)
public class AlcoholDocument {
    @Id
    private String id;

    @Field(type = FieldType.Long, index = false, docValues = false)
    private Long alcoholId;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String title;

    @Field(type = FieldType.Keyword, index = false, docValues = false)
    private String category;

    @Field(type = FieldType.Keyword, index = false, docValues = false)
    private BigDecimal degree;

    @Field(type = FieldType.Object)
    private List<TagDocument> tags = new ArrayList<>();

    @Field(type = FieldType.Object)
    @WriteOnlyProperty
    private List<AlcSearchKeyDocument> searchKeys = new ArrayList<>();

    @Builder
    private AlcoholDocument(Long alcoholId, String title, String category, BigDecimal degree) {
        this.alcoholId = alcoholId;
        this.title = title;
        this.category = category;
        this.degree = degree;
    }

    public void addTag(TagDocument tag) {
        tags.add(tag);
    }

    public void addSearchKey(AlcSearchKeyDocument searchKey) {
        searchKeys.add(searchKey);
    }
}