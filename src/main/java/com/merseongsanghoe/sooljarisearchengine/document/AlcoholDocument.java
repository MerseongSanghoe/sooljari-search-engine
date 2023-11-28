package com.merseongsanghoe.sooljarisearchengine.document;

import com.merseongsanghoe.sooljarisearchengine.entity.Alcohol;
import com.merseongsanghoe.sooljarisearchengine.node.AlcoholNode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Document(indexName = "alcohols", createIndex = false)
@Setting(replicas = 0)
public class AlcoholDocument {
    @Id
    private Long alcoholId;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String title;

    @Field(type = FieldType.Keyword, index = false, docValues = false)
    private String category;

    @Field(type = FieldType.Keyword, index = false, docValues = false)
    private BigDecimal degree;

    @Field(type = FieldType.Keyword, index = false, docValues = false)
    private String image;

    @Field(type = FieldType.Object)
    private List<TagDocument> tags = new ArrayList<>();

    @Field(type = FieldType.Object)
    @WriteOnlyProperty
    private List<AlcSearchKeyDocument> searchKeys = new ArrayList<>();

    private AlcoholDocument(Long alcoholId, String title, String category, BigDecimal degree, String image,
                            List<TagDocument> tags, List<AlcSearchKeyDocument> searchKeys) {
        this.alcoholId = alcoholId;
        this.title = title;
        this.category = category;
        this.degree = degree;
        this.image = image;
        this.tags = tags;
        this.searchKeys = searchKeys;
    }

    /**
     * Alcohol 엔티티 객체와 Alcohol 노드 엔티티 객체를 매개변수로 받아
     * 클래스 객체를 생성하는 스태틱 팩토리 메소드
     * @param alcohol Alcohol 엔티티 객체
     * @param alcoholNode Alcohol 노드 엔티티 객체
     * @return 생성한 Alcohol 도큐먼트 객체
     */
    public static AlcoholDocument of(Alcohol alcohol, AlcoholNode alcoholNode) {
        // image list에 데이터가 있다면 첫 번째 값, 없다면 null
        String imageURL = alcohol.getImages().isEmpty() ? null : alcohol.getImages().get(0).getImage().getUrl();

        return new AlcoholDocument(
                alcohol.getId(),
                alcohol.getTitle(),
                alcohol.getCategory(),
                alcohol.getDegree(),
                imageURL,
                alcoholNode.getTags().stream()
                                    .map(TagDocument::of)
                                    .collect(Collectors.toList()),
                alcohol.getSearchKeys().stream()
                                    .map(alcSearchKey -> new AlcSearchKeyDocument(alcSearchKey.getKey()))
                                    .collect(Collectors.toList())
        );
    }
}
