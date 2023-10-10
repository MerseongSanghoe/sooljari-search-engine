package com.merseongsanghoe.sooljarisearchengine.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "tags")
public class Tag {
    @Id
    private Long id;

    private String title;
}
