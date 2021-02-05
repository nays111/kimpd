package com.clnine.kimpd.src.Web.category.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data @Entity @Table(name="GenreCategory")
public class GenreCategory {
    @Id
    @Column(name="genreCategoryIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int genreCategoryIdx;

    @Column(name="genreCategoryName")
    private String genreCategoryName;
}
