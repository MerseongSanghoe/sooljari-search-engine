package com.merseongsanghoe.sooljarisearchengine.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * SearchHit<AlcoholDocument> 객체를 ResponseEntity에 적재할 형태로 변형한 DTO 객체
 */
@Getter
@Setter
public class AlcoholDTO {
    private float score;

    private Long id;

    private String title;
    private String category;
    private BigDecimal degree;
    private List<String> tags;
}
