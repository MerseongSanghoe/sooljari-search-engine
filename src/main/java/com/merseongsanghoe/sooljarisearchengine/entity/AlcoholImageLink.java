package com.merseongsanghoe.sooljarisearchengine.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "alcohols_images_links")
public class AlcoholImageLink {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "alcohol_id")
    private Alcohol alcohol;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;
}
