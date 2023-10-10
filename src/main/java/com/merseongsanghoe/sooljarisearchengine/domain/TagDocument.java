package com.merseongsanghoe.sooljarisearchengine.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagDocument {
    @Field(type = FieldType.Keyword, index = false, docValues = false)
    private String title;
}
