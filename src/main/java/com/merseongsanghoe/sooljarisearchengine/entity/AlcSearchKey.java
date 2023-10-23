package com.merseongsanghoe.sooljarisearchengine.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "alc_search_keys")
public class AlcSearchKey {
    @Id
    private Long id;

    @Column(name = "search_key")
    private String key;
}
