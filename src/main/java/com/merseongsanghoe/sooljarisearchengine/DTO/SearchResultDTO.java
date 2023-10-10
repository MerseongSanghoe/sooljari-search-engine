package com.merseongsanghoe.sooljarisearchengine.DTO;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    private List<String> tags = new ArrayList<>();

    @Builder
    private SearchResultDTO(float score, long id, String title, String category, BigDecimal degree) {
        this.score = score;
        this.id = id;
        this.title = title;
        this.category = category;
        this.degree = degree;
    }

    public void addTag(String tag) {
        tags.add(tag);
    }
}
