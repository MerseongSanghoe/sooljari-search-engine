package com.merseongsanghoe.sooljarisearchengine.document;

import com.merseongsanghoe.sooljarisearchengine.node.TagLink;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
public class TagDocument {
    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Integer, index = false, docValues = false)
    private Integer weight;

    private TagDocument(String title, Integer weight) {
        this.title = title;
        this.weight = weight;
    }

    public static TagDocument of(TagLink tagLink) {
        return new TagDocument(
                tagLink.getTag().getTitle(),
                tagLink.getWeight());
    }
}
