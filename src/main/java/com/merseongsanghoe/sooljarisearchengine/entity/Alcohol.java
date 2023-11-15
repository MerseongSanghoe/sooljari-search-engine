package com.merseongsanghoe.sooljarisearchengine.entity;

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
            name = "alc_search_keys_alcohol_links",
            joinColumns = @JoinColumn(name = "alcohol_id"),
            inverseJoinColumns = @JoinColumn(name = "alc_search_key_id")
    )
    private List<AlcSearchKey> searchKeys = new ArrayList<>();

    @OneToMany(mappedBy = "alcohol")
    @OrderBy("image_order")
    private List<AlcoholImageLink> images = new ArrayList<>();
}
