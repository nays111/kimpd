package com.clnine.kimpd.src.Web.category.models;

import com.clnine.kimpd.src.Web.user.models.UserInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table(name="UserGenreCategory")
public class UserGenreCategory {

    @Id
    @Column(name="userGenreCategoryIdx",nullable = false,updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userGenreCategoryIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userIdx")
    private UserInfo userInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="genreCategoryIdx")
    private GenreCategory genreCategory;


    public UserGenreCategory(UserInfo userInfo, GenreCategory genreCategory) {
        this.userInfo = userInfo;
        this.genreCategory = genreCategory;
    }
}
