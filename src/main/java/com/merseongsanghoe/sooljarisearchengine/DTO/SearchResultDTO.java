package com.merseongsanghoe.sooljarisearchengine.DTO;

import com.merseongsanghoe.sooljarisearchengine.document.AlcoholDocument;
import com.merseongsanghoe.sooljarisearchengine.document.TagDocument;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 검색 결과인 SearchHit<AlcoholDocument> 객체를 ResponseEntity에 적재할 형태로 변형한 DTO 객체
 */
@Getter
public class SearchResultDTO {
    private float score;

    private Long id;

    private String title;
    private String category;
    private BigDecimal degree;
    private List<String> tags;

    private SearchResultDTO(float score, long id, String title, String category, BigDecimal degree,
                            List<String> tags) {
        this.score = score;
        this.id = id;
        this.title = title;
        this.category = category;
        this.degree = degree;
        this.tags = tags;
    }

    /**
     * SearchHit<AlcoholDocument> 객체에서 score와 AlcoholDocument 객체를 받아서
     * 클래스 객체를 생성하는 스태틱 팩토리 메소드
     * @param score
     * @param document
     */
    public static SearchResultDTO of(float score, AlcoholDocument document) {
        return new SearchResultDTO(
                score,
                document.getAlcoholId(),
                document.getTitle(),
                document.getCategory(),
                document.getDegree(),
                document.getTags().stream().map(TagDocument::getTitle).collect(Collectors.toList())
        );
    }
}
