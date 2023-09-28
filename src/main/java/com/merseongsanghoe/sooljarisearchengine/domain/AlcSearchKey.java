package com.merseongsanghoe.sooljarisearchengine.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "alc_search_keys")
public class AlcSearchKey {
    @Id
    private Long id;

    private String key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alcohol_id")
    private Alcohol alcohol;
}
