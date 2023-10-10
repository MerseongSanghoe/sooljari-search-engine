package com.merseongsanghoe.sooljarisearchengine.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "alcohols")
public class Alcohol {
    @Id
    private Long id;

    private String title;

    private String category;

    private BigDecimal degree;

    @ManyToMany
    @JoinTable(
            name = "alcohols_tags_links",
            joinColumns = @JoinColumn(name = "alcohol_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "alc_search_keys_alcohol_links",
            joinColumns = @JoinColumn(name = "alcohol_id"),
            inverseJoinColumns = @JoinColumn(name = "alc_search_key_id")
    )
    private List<AlcSearchKey> searchKeys = new ArrayList<>();
}
