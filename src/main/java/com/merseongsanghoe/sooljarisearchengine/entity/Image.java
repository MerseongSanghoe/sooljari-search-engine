package com.merseongsanghoe.sooljarisearchengine.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "images")
public class Image {
    @Id
    private Long id;

    private String url;
}
